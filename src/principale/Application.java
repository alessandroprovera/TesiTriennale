package principale;

import java.util.LinkedList;
import java.util.TreeMap;

public class Application {

	public static void main(String[] args) {
		
		// creo l'istanza
		// pi: lista di interi contenente l'ordine delle macchine
		// t: intero che dice quante macchine stanno sulla riga 1
		// r,s interi che indicano l'offset di partenza (uno dei due sta a 0)
		// c: matrice dei costi#
		// dist: mappa delle distanze per ogni macchina
		
		LinkedList<Integer> pi = new LinkedList<Integer>();
		pi.add(1); pi.add(2); pi.add(6); pi.add(3); pi.add(4); pi.add(5);
		int t = 3;
		int r = 0;
		int s = 2;
		int c[][] = {{0,30,25,27,36,40},
					 {30,0,36,38,41,22},
					 {25,36,0,26,29,33},
					 {27,38,26,0,46,50},
					 {36,41,29,46,0,43},
					 {40,22,33,50,43,0}};
		// faccio un'altra copia, poi dovrò duplicarla all'interno della funzione
		int tempC[][] = {{0,30,25,27,36,40},
						{30,0,36,38,41,22},
						{25,36,0,26,29,33},
						{27,38,26,0,46,50},
						{36,41,29,46,0,43},
						{40,22,33,50,43,0}};
		TreeMap<Integer,Integer> dist = new TreeMap<Integer,Integer>();
		dist.put(1, 4); dist.put(2, 10); dist.put(3, 12); dist.put(4, 18); dist.put(5, 6); dist.put(6, 14);
		System.out.println("\n\nIl costo totale di questa configurazione e: " +  calcolaCosto(pi,t,r,s,dist,c));
		
		System.out.println("Ottimizzo la configurazione:");
		piOttimizzato(pi,c);
	
	}
	
	// funzione che calcola la funzione obiettivo
	
	public static int calcolaCosto(LinkedList<Integer> pi, int t, int r, int s, TreeMap<Integer,Integer> dist,int c[][]) {
		
		// calcolo i centri delle macchine in base a t e r,s
		//divido le due file in base a t
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
			int centro = k + r + dist.get(i)/2;
			k += dist.get(i);
			centri1.put(i,centro);
		}
		
		System.out.println("\n\nCentri:");
		System.out.print("Fila 1: ");
		for(Integer i: centri1.values()){
			System.out.print(i + ", ");
		}
		
		k = 0;
		for(Integer i: fila2) {
			int centro = k + s + dist.get(i)/2;
			k += dist.get(i);
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
		
		
		//rendo la matrice dei costi triangolare superiore
		System.out.println("\n\nMatrice dei costi triangolare superiore:");
		for(Integer i: centri.keySet()) {
			for(Integer z: centri.keySet()) {
				if(i>z)
					c[i-1][z-1] = 0;
					System.out.print(c[i-1][z-1] + "\t");
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
		
		// provo a scrivere l'algoritmo seguente per migliorare la funzione obiettivo.
		// scorro tutta la matrice dei costi e identifico il costo maggiore
		// una volta identificato pongo le due macchine che generano quel costo una davanti all'altra nella nuova configurazione
		// procedo in questo modo (scegliendo sempre il costo maggiore) fino ad esaurimento delle coppie (implemento
		// prima una versione per macchine pari)
		
		
		return fObiettivo;
	}
	
	public static LinkedList<Integer> piOttimizzato (LinkedList<Integer> pi,int c[][]){
		 
		// voglio identificare il massimo e la posizione della matrice dei costi
		LinkedList<Integer> fila1 = new LinkedList<Integer>();
		LinkedList<Integer> fila2 = new LinkedList<Integer>();
		
		//ricordiamoci che la matrice c è gia triangolare superiore
		
		// siccome ad ogni ciclo assegno due macchine, ciclo pi.size/2 volte per assegnarle tutte
		int k = 0;
		while(k<(pi.size()/2)) {
			
			int max = 0;
			LinkedList<Integer> massimiTrovati = new LinkedList<Integer>();
			for(int i = 0; i < pi.size(); i++) {
				for(int j = 0; j < pi.size(); j++) {
					if(c[i][j] > max && !massimiTrovati.contains(c[i][j])) {
						max = c[i][j];
						massimiTrovati.add(max);
					}
						
				}
			}
			// riciclo e cerco il punto in cui ce quel massimo
			for(int i = 0; i < pi.size(); i++) {
				for(int j = 0; j < pi.size(); j++) {
					if(c[i][j] == max) {
						fila1.add(i+1);
						fila2.add(j+1);
					}
						
				}
			}
			k++;
		}
		System.out.println(fila1.toString());
		System.out.println(fila2.toString());
		
		
		
		
		
		return null;
	}

}
