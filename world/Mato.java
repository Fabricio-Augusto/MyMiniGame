package world;

import java.awt.image.BufferedImage;

import main.Game;

public class Mato extends Tile{

	public Mato(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}
	
	public static boolean checarMato(Tile e) {
		int x = Game.player.getX()+8;
		int y = Game.player.getY()+8;
		int x2= e.getX()+8;
		int y2= e.getY()+8;
		if( (x-x2)<=8 && (x-x2)>=-8 ) {
			if( (y-y2)<=8 && (y-y2)>=-8 ) {
				return true;
			}
		}
		return false;
	}
}
