package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import main.Sound;
import world.AStar;
import world.Camera;
import world.Vector2i;

public class Enemy extends Entity {

	protected int speed = 1;
	protected int xx2= Game.player.x, xx1, yy2= Game.player.y, yy1;
	protected int frames = 0, maxFrames = 5, index =	0, maxIndex = 1;
	protected int contDelay;
	protected boolean delay=false;
	protected BufferedImage[] sprites;
	protected int life;
	protected boolean isDamaged = false;
	protected int damageFrames = 10, damageCurrent = 0;
	protected int EnemyDamage;
	
	public Enemy(int x, int y, int width, int height, int life, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.life = life;
		this.EnemyDamage=4;
		//Frames de Animação
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}
	
	public int getLife() {
		return this.life;
	}
	
	public void setLife(int newLife) {
		this.life = newLife;
	}
	
	public void tick() {
		depth =0;
		if(contDelay%10 == 0) {
			xx1 = xx2;
			xx2 = Game.player.x;
			yy1 = yy2;
			yy2 = Game.player.y;
		}

		contDelay++;
		if(contDelay > 150) {
			delay = true;
		}
		
		//implementando o A algoritime
		if((this.CalculoDeDistancia(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 150) && delay && Game.player.z==0) {
			if(!Game.player.escondido) {
				if(!isColliddingWithPlayer()) {
					if(path == null || path.size() == 0 || (Math.sqrt( (xx1 - xx2)*(xx1 - xx2) + (yy1 - yy2)*(yy1 - yy2) ) > 10)) {
						Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
						Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
						path = AStar.findPath(Game.world, start, end);
					}
				}else 
				{
					if(Game.player.z == 0) 
					{	
						if(Game.player.getShield()!=null && Game.player.getShield().getLife()>0 && Game.player.defendendo) 
						{	
							if(Game.random.nextInt(100) < 10) {
								Sound.hurt.play();
								Game.player.getShield().setLife( Game.player.getShield().getLife() - Game.random.nextInt(EnemyDamage) ); 
							}
						}	
						else 
						{	
							if(Game.random.nextInt(100) < 10) {
								Sound.hurt.play();
								Game.player.life -= Game.random.nextInt(EnemyDamage);
								Game.player.isDamaged = true;
							}
						}
					}
				}
				followPath(path);
			}
		}
		//Animation
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
		
		collidingSword();
		collidingAxe();
		
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	

	
	public void collidingSword() {
		if(Game.player.getSword() != null) {
			if(isColliddingWithBlade() && Game.player.getSword().getUse()) {
				isDamaged = true;
				Game.player.getSword().cut(this);
				Game.player.getSword().setUse(false);
				System.out.println("colidi");
				return;
			}
		}
	}
	
	public void collidingAxe() {
		if(Game.player.getAxe() != null) {
			if(isColliddingWithBlade() && Game.player.getAxe().getUse()) {
				isDamaged = true;
				Game.player.getAxe().cut(this);
				Game.player.getAxe().setUse(false);
				System.out.println("colidi");
				return;
			}
		}
	}
	public boolean isColliddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliddingWithBlade() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		if(Game.player.dir==0) {
			Rectangle MySword = new Rectangle(Game.player.getX()+13, Game.player.getY(), 6, 16);
			return enemyCurrent.intersects(MySword);
		}else {
			Rectangle MySword = new Rectangle(Game.player.getX()-3, Game.player.getY(), 6, 16);
			return enemyCurrent.intersects(MySword);
		}
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.setColor(Color.red);
			g.fillRect(this.getX() - Camera.x + 6, this.getY() - Camera.y , 5, 1);
			g.setColor(Color.green);
            if(life==10)
				g.fillRect(this.getX() - Camera.x + 6, this.getY() - Camera.y , 5, 1);
            else if(life==8)
            	g.fillRect(this.getX() - Camera.x + 6, this.getY() - Camera.y , 4, 1);
            else if(life==6)
            	g.fillRect(this.getX() - Camera.x + 6, this.getY() - Camera.y , 3, 1);
            else if(life==4)
            	g.fillRect(this.getX() - Camera.x + 6, this.getY() - Camera.y , 2, 1);
            else
            	g.fillRect(this.getX() - Camera.x + 6, this.getY() - Camera.y , 1, 1);
			g.drawImage(sprites[index], this.getX()- Camera.x, this.getY()- Camera.y, null);
		}else
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX()- Camera.x, this.getY()- Camera.y, null);
	}
}
