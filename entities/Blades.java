package entities;

import java.awt.image.BufferedImage;

import main.Game;

public class Blades extends Entity{

	private int damage;
	private double weight;
	private double life;
	private boolean use;
	private boolean canUse=true;
	
	public Blades(int damage, double weight, int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.damage = damage;
		this.weight = weight;
		this.life = 100;
		this.use = false;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public double getLife() {
		return this.life;
	}
	
	public boolean getUse() {
		return this.use;
	}
	
	public boolean getcanUse() {
		return this.canUse;
	}
	
	public void setDamage(int newDamage) {
		this.damage = newDamage;
	}
	
	public void setLife(double newLife) {
		this.life = newLife;
	}
	
	public void setUse(boolean use) {
		this.use = use;
	}
	
	public void setcanUse(boolean canUse) {
		this.canUse = canUse;
	}
	public void flaw() {
		this.setLife((this.life - 0.7));
	}
	
	public void cut(Enemy enemy) {
		enemy.setLife(enemy.getLife() - damage);
		/*if(Game.player.dir==0) {
			enemy.setX(enemy.getX() + 8);
		}else {
			enemy.setX(enemy.getX() - 8);
		}*/
	}

}
