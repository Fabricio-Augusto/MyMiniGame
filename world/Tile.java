package world;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;

public class Tile {
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	public static BufferedImage TILE_MINI_WALL = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage TILE_MATO = Game.spritesheet.getSprite(48, 32, 16, 16);
	public static BufferedImage TILE_ARVORE1 = Game.spritesheet.getSprite(64, 32, 16, 16);
	public static BufferedImage TILE_ARVORE2 = Game.spritesheet.getSprite(80, 32, 16, 16);
	public static BufferedImage TILE_ARVORE3 = Game.spritesheet.getSprite(64, 48, 16, 16);
	public static BufferedImage TILE_ARVORE4 = Game.spritesheet.getSprite(80, 48, 16, 16);
	public static BufferedImage TILE_MATO_NIGHT = Game.spritesheet.getSprite(0, 112, 16, 16);
	public static BufferedImage TILE_FLOOR_NIGHT = Game.spritesheet.getSprite(0, 96, 16, 16);
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public static boolean isCollidding(Tile e1 , Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() , e1.getY(),16 , 16);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.getmaskx(), e2.getY() + e2.getmasky(),3, 3);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
