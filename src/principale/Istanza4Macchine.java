package principale;

import java.util.LinkedList;
import java.util.TreeMap;

public class Istanza4Macchine {

	public static void main(String[] args) {
		
		// creo l'istanza
		// pi: lista di interi contenente l'ordine delle macchine
		// t: intero che dice quante macchine stanno sulla riga 1
		// r,s interi che indicano l'offset di partenza (uno dei due sta a 0)
		// c: matrice dei costi#
		// pos: mappa delle posizioni della macchina a partire dall'origine 0
		
		LinkedList<Integer> pi = new LinkedList<Integer>();
		// ordine delle macchine
		pi.add(1); pi.add(3); pi.add(4); pi.add(2);
		// setto t,r,s
		final int t = 2;
		final int r = 0;
		final int s = 1;
		// setto la matrice dei costi triangolare superiore
		int c[][] = {{0,23,27,33},
					 {0,0,45,36},
					 {0,0,0,20},
					 {0,0,0,0}};
		
		// faccio la mappa delle posizioni
		final TreeMap<Integer,Integer> pos = new TreeMap<Integer,Integer>();
		pos.put(1, 4); pos.put(2, 2); pos.put(3, 2); pos.put(4, 6);
		
		System.out.println("Calcolo la funzione obiettivo dell'istanza INIZIALE: \n");
		System.out.println("\n\nIl costo totale della configurazione iniziale e: " +  calcolaCosto(pi,t,r,s,pos,c));
		
		System.out.println("\nCalcolo la funzione obiettivo dell'istanza OTTIMIZZATA: \n");
		System.out.println("\n\nIl costo totale della configurazione ottimizzata e: " + calcolaCosto(piOttimizzato(pi,c),t,r,s,pos,c));
	
	}
	
	// funzione che calcola la funzione obiettivo data un istanza del problema, 
	// NON modifica in alcune modo le variabili in ingresso
	
	public static int calcolaCosto(LinkedList<Integer> pi, int t, int r, int s, TreeMap<Integer,Integer> pos,int c[][]) {
		
		// calcolo i centri delle macchine in base a t e r,s
		//divido le due file in base a t
		// creo una variabile di lavoro tempPi con gli stessi elementi di pi
		LinkedList<Integer> tempPi = new LinkedList<Integer>();
		for(Integer integer: pi) {
			tempPi.add(integer);
		}
		LinkedList<Integer> fila1 = new LinkedList<Integer>();
		LinkedList<Integer> fila2 = new LinkedList<Integer>();
		int j=0;
		while(j<t) {
			fila1.add(tempPi.getFirst());
			tempPi.removeFirst();
			j++;
		}
		fila2.addAll(tempPi);
		
		System.out.println("Divisione in file:");
		System.out.print("Fila 1: ");
		for(Integer i: fila1){
			System.out.print(i + ", ");
		}
		System.out.print("\nFila 2: ");
		for(Integer i: fila2){
			System.out.print(i + ", ");
		}
		
		//calcolo i centri delle macchine di ogni fila
		TreeMap<Integer,Integer> centri1 = new TreeMap<Integer,Integer>();
		TreeMap<Integer,Integer> centri2 = new TreeMap<Integer,Integer>();
		int k = r; // a che punto sono arrivato
		for(Integer i: fila1) {
			int centro = k + r + pos.get(i)/2;
			k += pos.get(i);
			centri1.put(i,centro);
		}
		
		System.out.println("\n\nCentri:");
		System.out.print("Fila 1: ");
		for(Integer i: centri1.values()){
			System.out.print(i + ", ");
		}
		
		k = 0;
		for(Integer i: fila2) {
			int centro = k + s + pos.get(i)/2;
			k += pos.get(i);
			centri2.put(i,centro);
		}
		
		System.out.print("\nFila 2: ");
		for(Integer i: centri2.values()){
			System.out.print(i + ", ");
		}
		
		System.out.println("\n\nMatrice delle distanze:");
		//calcolo le distanze in una matrice
		// creo una mappa ordinata dei centri
		TreeMap<Integer,Integer> centri = new TreeMap<Integer,Integer>();
		centri.putAll(centri1);
		centri.putAll(centri2);
		int distance[][] = new int[centri.size()][centri.size()];
		for(Integer i: centri.keySet()) {
			for(Integer z: centri.keySet()) {
				distance[i-1][z-1] = Math.abs(centri.get(i)-centri.get(z));
				System.out.print(distance[i-1][z-1] + "\t");
			}
			System.out.print("\n");
		}
		
		//rendo la matrice delle distanze triangolare superiore
		System.out.println("\n\nMatrice delle distanze triangolare superiore:");
		for(Integer i: centri.keySet()) {
			for(Integer z: centri.keySet()) {
				if(i>z)
					distance[i-1][z-1] = 0;
				System.out.print(distance[i-1][z-1] + "\t");
			}
			System.out.print("\n");
		}
		
		//moltiplico la matrice delle distanze per quella dei costi
		int fObiettivo = 0;
		for(Integer i: centri.keySet()) {
			for(Integer z: centri.keySet()) {
				fObiettivo += distance[i-1][z-1]*c[i-1][z-1];
			}
		}
		
		return fObiettivo;
	}
	
	public static LinkedList<Integer> piOttimizzato (LinkedList<Integer> pi,int c[][]){
		 
		// provo a scrivere l'algoritmo seguente per migliorare la funzione obiettivo.
				// scorro tutta la matrice dei costi e identifico il costo maggiore
				// una volta identificato pongo le due macchine che generano quel costo una davanti all'altra nella nuova configurazione
				// procedo in questo modo (scegliendo sempre il costo maggiore) fino ad esaurimento delle coppie (implemento
				// prima una versione per macchine pari)
		// voglio identificare il massimo e la posizione della matrice dei costi
		LinkedList<Integer> fila1 = new LinkedList<Integer>();
		LinkedList<Integer> fila2 = new LinkedList<Integer>();
		
		// siccome ad ogni ciclo assegno due macchine, ciclo pi.size/2 volte per assegnarle tutte
		int k = 0;
		LinkedList<Integer> posMacchineAssegnate = new LinkedList<Integer>();
		while(k<(pi.size()/2)) {
			int max = 0;
			int posIMax = 0;
			int posJMax = 0;
			// cerco il massimo nella matrice dei costi
			for(int i = 0; i < pi.size(); i++) {
				for(int j = 0; j < pi.size(); j++) {
					if(c[i][j] > max && !posMacchineAssegnate.contains(i) && !posMacchineAssegnate.contains(j)) {
						max = c[i][j];
						posIMax = i;
						posJMax = j;
					}
						
				}
			}
			posMacchineAssegnate.add(posIMax);
			posMacchineAssegnate.add(posJMax);
			fila1.add(posIMax+1);
			fila2.add(posJMax+1);

			k++;
		}
		
		LinkedList<Integer> configurazioneOttimizzata = new LinkedList<Integer>();
		configurazioneOttimizzata.addAll(fila1);
		configurazioneOttimizzata.addAll(fila2);
		
		return configurazioneOttimizzata;
	}

}
