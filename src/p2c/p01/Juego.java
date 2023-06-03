package p2c.p01;

import java.util.Hashtable;

public class Juego implements IJuego {

	private boolean creaEnemigo = false;
	private boolean eliminaEnemigo = false;

	private int contadorEnemigosTotales = 0;
	private Hashtable<Integer, Integer> contadoresEnemigosTipo;
	private Hashtable<Integer, Integer> contadoresEnemigosEliminadosTipo;

	private int MAXENEMIGOS = 50;
	private int MINENEMIGOS = 0;

	private boolean bloqueoOperacion = false;

	public Juego() {
		contadorEnemigosTotales = 0;
		contadoresEnemigosTipo = new Hashtable<Integer, Integer>();
		contadoresEnemigosEliminadosTipo = new Hashtable<Integer, Integer>();
	}

	@Override
	public synchronized void generarEnemigo(int tipo) {
		if (contadoresEnemigosTipo.get(tipo) == null) {
			contadoresEnemigosTipo.put(tipo, 0);
			contadoresEnemigosEliminadosTipo.put(tipo, 0);
		}

		comprobarAntesDeGenerar(tipo);

		if (creaEnemigo = true) {
			bloqueoOperacion = true;
			contadorEnemigosTotales++;
			contadoresEnemigosTipo.put(tipo, contadoresEnemigosTipo.get(tipo) + 1);
			imprimirInfo(tipo, "Generado");
			checkInvariante();
			bloqueoOperacion = false;
			creaEnemigo = false;
			notifyAll();
		}

	}

	@Override
	public synchronized void eliminarEnemigo(int tipo) {

		comprobarAntesDeEliminar(tipo);
		if (eliminaEnemigo = true) {
			bloqueoOperacion = true;
			contadorEnemigosTotales--;
			contadoresEnemigosTipo.put(tipo, contadoresEnemigosTipo.get(tipo) - 1);
			contadoresEnemigosEliminadosTipo.put(tipo, contadoresEnemigosEliminadosTipo.get(tipo) + 1);
			imprimirInfo(tipo, "Eliminado");
			checkInvariante();
			bloqueoOperacion = false;
			eliminaEnemigo = false;
			notifyAll();

		}

	}

	public int sumarContadores() {
		int suma = 0;
		for (int i = 0; i < contadoresEnemigosTipo.size(); i++) {
			suma += contadoresEnemigosTipo.get(i);
		}
		return suma;
	}

	private void imprimirInfo(int tipo, String operacion) {
		System.out.println(operacion + " enemigo tipo " + tipo);
		System.out.println("--> Enemigos Totales: " + contadorEnemigosTotales);

		for (int p : contadoresEnemigosTipo.keySet()) {

			System.out.println("---> Enemigos Tipo " + p + ": " + contadoresEnemigosTipo.get(p)
					+ " ------ [Eliminados: " + contadoresEnemigosEliminadosTipo.get(p) + "]");

		}
		System.out.println(" ");
	}

	protected void comprobarAntesDeGenerar(int tipo) {
		boolean contiene = true;
			
		if (contadoresEnemigosTipo.containsKey(tipo)) {
			for (int i = 0; i < tipo; i++) {
				if(!contadoresEnemigosTipo.containsKey(i)) {
					contiene = false;
				}
			}
		}
		
		if (contiene) {
			creaEnemigo = true;
			contiene = true;
		}

		while (!creaEnemigo || bloqueoOperacion || contadorEnemigosTotales == MAXENEMIGOS) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.toString());
			}
		}

	}

	protected void comprobarAntesDeEliminar(int tipo) {
		if (contadoresEnemigosEliminadosTipo.containsKey(tipo) && contadoresEnemigosTipo.get(tipo)>0) {
			if(contadorEnemigosTotales > MINENEMIGOS) {
				eliminaEnemigo = true;
			}
		}
		
		while (!eliminaEnemigo || bloqueoOperacion || contadorEnemigosTotales == MINENEMIGOS) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.toString());
			}
		}
	}

	protected void checkInvariante() {
		assert sumarContadores() == contadorEnemigosTotales
				: "INV: La suma de los contadores de cada tipo tiene que ser igual al contador total";

	}

}
