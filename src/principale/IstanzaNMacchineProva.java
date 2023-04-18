package principale;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

public class IstanzaNMacchineProva {
	
	// t: intero che dice quante macchine stanno sulla riga 1
	// r,s interi che indicano l'offset di partenza (uno dei due sta a 0)
	// setto t,r,s come costanti
	final static int N = 50;
	final static int seed = 0;
	final static int t = N/2;
	final static int r = 0;
	final static int s = 1;
	
	// faccio la mappa delle dimensioni macchine
	final static Map<Integer, Integer> dimensioniMacchine;
	    static {
	    	dimensioniMacchine = new HashMap<>();
	    	List<Integer> daRiempire = new ArrayList<Integer>();
	    	daRiempire = generaNumeriRandom(seed,2,10,N);
	    	int cnt = 1;
	    	for(int i: daRiempire) {
	    		dimensioniMacchine.put(cnt,i*2);
	    		cnt++;
	    	}
	    }

	public static void main(String[] args) {
		
		// creo l'istanza
		// pi: lista di interi contenente l'ordine delle macchine
		// c: matrice dei costi#
		
		LinkedList<Integer> pi = new LinkedList<Integer>();
		// ordine delle macchine
		for(int i=1; i<=N; i++) {
			pi.add(i);
		}
		// setto la matrice dei costi triangolare superiore
		int c[][] = new int[N][N];
		List<Integer> costiRandom = new ArrayList<Integer>();
		costiRandom = generaNumeriRandom(seed,20,50,(N*N)/2);
		int k=0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(j>i) {
					c[i][j] = costiRandom.get(k);
					k++;
				}
					
			}
		}
		
		System.out.println("Genero la soluzione INIZIALE data l'istanza inserita " + pi.toString() + ": " + calcolaCosto(pi,c));
		LinkedList<Integer> soluzioneIniziale = new LinkedList<Integer>();
		for(Integer i: piSolIniziale(pi,c)) {
			soluzioneIniziale.add(i);
		}
		System.out.println("\nLa configurazione della soluzione INIZIALE e: " + soluzioneIniziale.toString());
		System.out.println("Il costo totale della soluzione INIZIALE e: " +  calcolaCosto(soluzioneIniziale,c));
		
		
		long start = System.nanoTime();
		LinkedList<Integer> soluzioneOttimizzata = new LinkedList<Integer>();
		for(Integer i: piOttimizzatoRicercaLocale(pi,c)) {
			soluzioneOttimizzata.add(i);
		}
		System.out.println("\nLa configurazione della soluzione OTTIMIZZATA e: " + soluzioneOttimizzata);
		long stop = System.nanoTime();
		System.out.println("Il costo totale della soluzione OTTIMIZZATA e: " + calcolaCosto(soluzioneOttimizzata,c));
		System.out.println("Il tempo totale della soluzione OTTIMIZZATA e: " + (stop-start)/1000000000.0 + " secondi");
	
	}
	
	// funzione che calcola la funzione obiettivo data un istanza del problema, 
	// NON modifica in alcune modo le variabili in ingresso
	
	public static int calcolaCosto(LinkedList<Integer> pi, int c[][]) {
		
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
		
		//calcolo i centri delle macchine di ogni fila
		TreeMap<Integer,Integer> centri1 = new TreeMap<Integer,Integer>();
		TreeMap<Integer,Integer> centri2 = new TreeMap<Integer,Integer>();
		int k = r; // a che punto sono arrivato
		for(Integer i: fila1) {
			int centro = k + r + dimensioniMacchine.get(i)/2;
			k += dimensioniMacchine.get(i);
			centri1.put(i,centro);
		}
		
		k = 0;
		for(Integer i: fila2) {
			int centro = k + s + dimensioniMacchine.get(i)/2;
			k += dimensioniMacchine.get(i);
			centri2.put(i,centro);
		}
		
		//calcolo le distanze in una matrice
		// creo una mappa ordinata dei centri
		TreeMap<Integer,Integer> centri = new TreeMap<Integer,Integer>();
		centri.putAll(centri1);
		centri.putAll(centri2);
		int distance[][] = new int[centri.size()][centri.size()];
		for(Integer i: centri.keySet()) {
			for(Integer z: centri.keySet()) {
				distance[i-1][z-1] = Math.abs(centri.get(i)-centri.get(z));
			}
		}
		
		//rendo la matrice delle distanze triangolare superiore
		for(Integer i: centri.keySet()) {
			for(Integer z: centri.keySet()) {
				if(i>z)
					distance[i-1][z-1] = 0;
			}
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
	
	public static LinkedList<Integer> piSolIniziale (LinkedList<Integer> pi,int c[][]){
		 
		// provo a scrivere l'algoritmo seguente per generare la soluzione iniziale.
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
	
	public static LinkedList<Integer> piOttimizzatoRicercaLocale(LinkedList<Integer> pi,int c[][]){
		
		// creo una variabile di lavoro tempPi con gli stessi elementi di pi
		LinkedList<Integer> tempPi = new LinkedList<Integer>();
		for(Integer integer: pi) {
			tempPi.add(integer);
		}
		
		//per ogni coppia di indici i,j calcolo la funzione obiettivo
		int min;
		LinkedList<Integer> daRestituire = new LinkedList<Integer>();
		Map<LinkedList<Integer>,Integer> mappaSoluzioni = new HashMap<LinkedList<Integer>,Integer>();
		for(int i = 0; i< pi.size(); i++) {
			for(int j = 0; j<pi.size();j++) {
				if(i!=j) {
					Collections.swap(tempPi, i, j);
					int costo = calcolaCosto(tempPi,c);
					LinkedList<Integer> daCopiare = new LinkedList<Integer>();
					for(Integer k: tempPi) {
						daCopiare.add(k);
					}
					mappaSoluzioni.put(daCopiare, costo);
				}
			}
		}
		min = mappaSoluzioni.get(tempPi);
		daRestituire = tempPi;
		for(LinkedList<Integer> config: mappaSoluzioni.keySet()) {
			if(mappaSoluzioni.get(config) < min) {
				min = mappaSoluzioni.get(config);
				daRestituire = config;
			}	
		}
		return daRestituire;
	}

	public static List<Integer> generaNumeriRandom(long seed, int start, int stop, int l){
		
		Random generator = new Random(seed);
		List<Integer> daRestituire = new ArrayList<Integer>();
		
		for(int i=0; i<l; i++) {
			int j = generator.nextInt(start, stop);
			daRestituire.add(j);
		}
		
		return daRestituire;
		
		
	}
}
