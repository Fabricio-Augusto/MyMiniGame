package entities;

import java.awt.image.BufferedImage;

public class Shield extends Entity{
	
	private int life;
	
	public Shield(int life, int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.life = life;
	}
	
	public int getLife() {
		return this.life;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public void flaw() {
		this.setLife((this.life - 10));
	}
	
}
