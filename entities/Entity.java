package entities;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import main.Game;
import world.Camera;
import world.Node;
import world.Vector2i;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	public static BufferedImage GUN_DAMAGE_RIGHT = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage GUN_DAMAGE_LEFT = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage WeaponSword = Game.spritesheet.getSprite(6*16, 32, 16, 16);
	public static BufferedImage WeaponSwordLeft = Game.spritesheet.getSprite(7*16, 32, 16, 16);
	public static BufferedImage imageBoss = Game.spritesheet.getSprite(64, 64, 16, 16);
	public static BufferedImage imageBossFeedback = Game.spritesheet.getSprite(80,64,16,16);
	public static BufferedImage imageShield = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage imageAxe = Game.spritesheet.getSprite(0, 80, 16, 16);
	public static BufferedImage imageRightAxe = Game.spritesheet.getSprite(32, 80, 16, 16);
	public static BufferedImage imageLeftAxe = Game.spritesheet.getSprite(16, 80, 16, 16);
	
	protected int x;
	protected int y;
	protected int z=0;
	protected int width;
	protected int height;
	
	public int depth;
	
	protected List<Node> path;
	
	protected BufferedImage sprite;
	
	public int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		
		public int compare(Entity n0, Entity n1) {
			if(n1.depth < n0.depth)
				return +1;
			if(n1.depth > n0.depth)
				return -1;
			return 0;
		}
	};
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getmaskx() {
		return maskx;
	}
	
	public int getmasky() {
		return masky;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	
	public void tick() {
		
	}
	
	public double CalculoDeDistancia(int x1, int y1, int x2, int y2) {
		return Math.sqrt( (x1 - x2)*(x1 - x2) + (y1 -y2)*(y1 -y2) );
	}
	
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, mwidth, mheight); //Classe que cria retangulos fict?cios para testar colis?es.
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if(e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, mwidth, mheight);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	
	public void followPath(List<Node> path) {
		if(path != null) {
			// se o tamhaho da lista de path for maior que 0 ainda tem caminho para percorrer
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x*16) {
					x++;
				} else if(x > target.x*16) {
					x--;
				}
				
				if(y < target.y*16) {
					y++;
				} else if(y > target.y*16) {
					y--;
				}
				
				if(x == target.x*16 && y == target.y*16) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY()+e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY()+e2.masky, e2.mwidth, e2.mheight);
		if((e1Mask.intersects(e2Mask)) && (e1.z == e2.z)) {
			return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY()+ masky - Camera.y, mwidth, height);
	}
}
