package p2c.p01;

public class SistemaLanzador {

	public SistemaLanzador() {

	}

	public static void main(String[] args) {
		IJuego juego = new Juego();

		System.out.println("¡Que comience la batalla!");

		for (int i = 0; i < Integer.parseInt(args[0]); i++) {

			ActividadEnemiga crear = new ActividadEnemiga(i, juego);
			new Thread(crear).start();

			ActividadAliada eliminar = new ActividadAliada(i, juego);
			new Thread(eliminar).start();

		}

	}
}
