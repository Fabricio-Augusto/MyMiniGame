package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Boss extends Enemy{
	
	private BufferedImage[] sprites;
	
	public Boss(int x, int y, int width, int height, int life, BufferedImage sprite) {
		super(x, y, width, height, life, sprite);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(64, 64, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(80, 64, 16, 16);
		super.EnemyDamage=6;
	}
	
	@Override
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX()- Camera.x, this.getY()- Camera.y, null);
		}else {
			g.drawImage(Entity.imageBossFeedback, this.getX()- Camera.x, this.getY()- Camera.y, null);
		}
	}
}
