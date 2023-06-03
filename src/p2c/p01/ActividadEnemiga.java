package p2c.p01;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActividadEnemiga implements Runnable{
	
	static Random numAleatorio = new Random();
	
	
	//Se creará un numero aleatorio de enemigos que ira desde los 0 a los 20
	public static final int MAX_ENEMIGOS = (int)numAleatorio.nextInt(20);
	private int tipo;
	private IJuego juego;
	
	public ActividadEnemiga(int tipo, IJuego juego) {
		this.tipo = tipo;
		this.juego = juego;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < MAX_ENEMIGOS; i++) {
			try {
				juego.generarEnemigo(tipo);
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5)*1000);
				
			} catch (InterruptedException e) {
					Logger.getGlobal().log(Level.INFO, "Se interrumpio la creacion de enemigos");
					Logger.getGlobal().log(Level.INFO, e.toString());
					return;
			}
		}
		
		
	}

}
