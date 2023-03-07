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
		TreeMap<Integer,Integer> dist = new TreeMap<Integer,Integer>();
		dist.put(1, 4); dist.put(2, 10); dist.put(3, 12); dist.put(4, 18); dist.put(5, 6); dist.put(6, 14);
		calcolaCosto(pi,t,r,s,dist);
	
	}
	
	// funzione che calcola la funzione obiettivo
	
	public static int calcolaCosto(LinkedList<Integer> pi, int t, int r, int s, TreeMap<Integer,Integer> dist) {
		
		// calcolo i centri delle macchine in base a t e r,s
		//divido le due file in base a t
		LinkedList<Integer> tempPi = pi;
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
		LinkedList<Integer> centri1 = new LinkedList<Integer>();
		LinkedList<Integer> centri2 = new LinkedList<Integer>();
		int k = r; // a che punto sono arrivato
		for(Integer i: fila1) {
			int centro = k + r + dist.get(i)/2;
			k += dist.get(i);
			centri1.add(centro);
		}
		
		System.out.println("\n\nCentri:");
		System.out.print("Fila 1: ");
		for(Integer i: centri1){
			System.out.print(i + ", ");
		}
		
		k = 0;
		for(Integer i: fila2) {
			int centro = k + s + dist.get(i)/2;
			k += dist.get(i);
			centri2.add(centro);
		}
		
		System.out.print("\nFila 2: ");
		for(Integer i: centri2){
			System.out.print(i + ", ");
		}
		
		
		return 0;
	}

}
