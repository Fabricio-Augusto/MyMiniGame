package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.Mato;
import world.Tile;
import world.World;

public class Player extends Entity {
	
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public int speed = 2;
	public double stamina=100;
	private int staminaFrames=25, staminaCur=0;
	private int frames = 0, maxFrames = 5, index =	0, maxIndex = 3;
	private int framesAttack=0, maxFramesAttack=4, indexAttack=0, maxIndexAttack=6;
	private int framesAttackAxe=0, maxFramesAttackAxe=8, indexAttackAxe=0, maxIndexAttackAxe=4;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] rightPlayerAttack;
	private BufferedImage[] leftPlayerAttack;
	private BufferedImage[] rightPlayerAttackAxe;
	private BufferedImage[] leftPlayerAttackAxe;
	private BufferedImage PlayerDamage;
	
	public double life = 100, maxLife = 100;
	public int mx, my;
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public boolean escondido = false;
	public boolean jump = false;
	public boolean isJumping = false;
	public boolean jumpUp=false, jumpDown=false;
	public int jumpFrames=50, jumpCur=0;
	public int jumpSpd=2;
	
	private boolean arma = false;
	private Sword sword;
	public boolean UseSword=false;
	private Shield shield;
	private boolean defesa = false;
	public boolean defendendo = false;
	private Axe axe;
	public boolean Axe = false;
	public boolean UseAxe=false;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		//Animações quantidade
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		rightPlayerAttack = new BufferedImage[7];
		leftPlayerAttack = new BufferedImage[7];
		rightPlayerAttackAxe = new BufferedImage[5];
		leftPlayerAttackAxe = new BufferedImage[5];
		PlayerDamage = Game.spritesheet.getSprite(0, 16, width, height);
		
		for(int i = 0; i< 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 +(i*16), 0, 16, 16);
		}
		
		for(int i = 0; i< 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 +(i*16), 16, 16, 16);
		}
		rightPlayerAttack[0] = Game.spritesheet.getSprite(0, 64, 16, 16);
		rightPlayerAttack[1] = Game.spritesheet.getSprite(16, 64, 16, 16);
		rightPlayerAttack[2] = Game.spritesheet.getSprite(32, 64, 16, 16);
		rightPlayerAttack[3] = Game.spritesheet.getSprite(48, 64, 16, 16);
		rightPlayerAttack[4] = Game.spritesheet.getSprite(32, 64, 16, 16);
		rightPlayerAttack[5] = Game.spritesheet.getSprite(16, 64, 16, 16);
		rightPlayerAttack[6] = Game.spritesheet.getSprite(0, 64, 16, 16);
		
		rightPlayerAttackAxe[0] = Game.spritesheet.getSprite(32, 80, 16, 16);
		rightPlayerAttackAxe[1] = Game.spritesheet.getSprite(64, 80, 16, 16);
		rightPlayerAttackAxe[2] = Game.spritesheet.getSprite(48, 80, 16, 16);
		rightPlayerAttackAxe[3] = Game.spritesheet.getSprite(64, 80, 16, 16);
		rightPlayerAttackAxe[4] = Game.spritesheet.getSprite(32, 80, 16, 16);
		
		leftPlayerAttack[0] = Game.spritesheet.getSprite(96,48,16,16);
		leftPlayerAttack[1] = Game.spritesheet.getSprite(112,48,16,16);
		leftPlayerAttack[2] = Game.spritesheet.getSprite(128,48,16,16);
		leftPlayerAttack[3] = Game.spritesheet.getSprite(144,48,16,16);
		leftPlayerAttack[4] = Game.spritesheet.getSprite(128,48,16,16);
		leftPlayerAttack[5] = Game.spritesheet.getSprite(112,48,16,16);
		leftPlayerAttack[6] = Game.spritesheet.getSprite(96,48,16,16);
		
		leftPlayerAttackAxe[0] = Game.spritesheet.getSprite(16, 80, 16, 16);
		leftPlayerAttackAxe[1] = Game.spritesheet.getSprite(80, 80, 16, 16);
		leftPlayerAttackAxe[2] = Game.spritesheet.getSprite(0, 80, 16, 16);
		leftPlayerAttackAxe[3] = Game.spritesheet.getSprite(80, 80, 16, 16);
		leftPlayerAttackAxe[4] = Game.spritesheet.getSprite(16, 80, 16, 16);
	}
	
	public boolean getArma() {
		return this.arma;
	}
	
	public void setArma(boolean arma) {
		this.arma = arma;
	}
	
	public Sword getSword() {
		return this.sword;
	}
	
	public Axe getAxe() {
		return this.axe;
	}
	
	public Shield getShield() {
		return this.shield;
	}
	
	public void tick() {
		depth =1;
		staminaCur++;
		
		if(Axe) {
			speed=1;
		}else {
			speed=2;
		}
		
		if(staminaCur == staminaFrames) {
			staminaCur=0;
			if(stamina <= 99) {	
				stamina++;
			}
		}
		
		if(jump) {
			if(isJumping == false) {	
				if(stamina>=20) {
					jump = false;
					isJumping = true;
					jumpUp=true;
					stamina-=20;
				}
				else
					jump=false;
			}
		}
		
		if(isJumping == true) {
			if(jumpUp) {
				jumpCur+=2;
			}else if(jumpDown) {
				jumpCur-=2;
				if(jumpCur <= 0) {
					isJumping = false;
					jumpUp = false;
					jumpDown = false;
				}
			}
			z = jumpCur;
			if(jumpCur >= jumpFrames) {
				jumpUp = false;
				jumpDown = true;
			}
		}
		
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY(), this.getZ())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed), this.getY(), this.getZ())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(), (int)(y-speed), this.getZ())) {
			moved = true;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(), (int)(y+speed), this.getZ())) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if(UseAxe) {
			framesAttackAxe++;
			if(framesAttackAxe == maxFramesAttackAxe) {
				framesAttackAxe=0;
				indexAttackAxe++;
				if(indexAttackAxe == maxIndexAttackAxe) {
					indexAttackAxe=0;
					axe.setcanUse(true);
					UseAxe=false;
				}
			}
		}
		
		if(Game.player.getAxe() != null && UseAxe==false ) {
			Game.player.getAxe().setUse(false);
		}
		
		if(UseSword) {
			framesAttack++;
			if(framesAttack == maxFramesAttack) {
				framesAttack=0;
				indexAttack++;
				if(indexAttack == maxIndexAttack) {
					indexAttack=0;
					sword.setcanUse(true);
					UseSword=false;
				}
			}
		}
		if(Game.player.getSword() != null && UseSword==false ) {
			Game.player.getSword().setUse(false);
		}
		
		checkCollisionMato();
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			checkCollisionLifePack(e);
			checkCollisionSword(e);
			checkCollisionShiled(e);
			checkCollisionAxe(e);
		}
		if(isDamaged) {
			this.damageFrames++;
			if(damageFrames == 10) {
				this.damageFrames = 0;
				isDamaged = false;
			}			
		}
				
		if(Game.player.life <= 0) {
			//Game over!
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		
		//Config Camera
		updateCamera();
		//if(sword != null && arma == true) {
		//	sword.ttick();
		//}
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
		
	//Coletar vida
	public void checkCollisionLifePack(Entity e) {
		if(e instanceof Lifepack) {
			if(Entity.isColidding(this, e)) {
				if(life<100) {
					life+=15;
					if(life >= 100) {
					life = 100;
					}
					Game.entities.remove(e);
					return;
				}
			}
		}
	}
	
	public void checkCollisionSword(Entity e) {
		if(e instanceof Sword) {
			if(Entity.isColidding(this, e)) {
				//arma = true;
				sword = (Sword) e;
				Game.entities.remove(e);
				System.out.println("Armei");
				return;
			}
		}
	}
	
	public void checkCollisionAxe(Entity e) {

		if(e instanceof Axe) {
			if(Entity.isColidding(this, e)) {
				//UseAxe = true;
				axe = (Axe) e;
				Game.entities.remove(e);
				return;
			}
		}
	}
	
	public void checkCollisionShiled(Entity e) {
		if(e instanceof Shield) {
			if(Entity.isColidding(this, e)) {
				defesa=true;
				shield = (Shield) e;
				Game.entities.remove(e);
				System.out.println("defendi");
				return;
			}
		}
	}
	
	public void checkCollisionMato() {
		for(int i=0; i < World.tiles.length -1; i++) {
			Tile CurTile = World.tiles[i];
			if(CurTile instanceof Mato) {	
				if(Mato.checarMato(CurTile)) {
					escondido=true;
					break;
				}
				else
					escondido=false;
			}
		}
	}
	
	
	public void render(Graphics g) {
		if(!isDamaged) {
		//Ativar o player no JFrame, flip
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if(arma) {
				//desenhar arma
				if(UseSword) {
					g.drawImage(rightPlayerAttack[indexAttack], this.getX() - Camera.x+6, this.getY() - Camera.y +4, null);
				}
				else if(!Axe){
				g.drawImage(Entity.WeaponSword, this.getX()+6 - Camera.x, this.getY() - Camera.y - z, null);
				}
			}
			if(Axe) {
				if(UseAxe) {
					g.drawImage(rightPlayerAttackAxe[indexAttackAxe], this.getX()+3 - Camera.x, this.getY()+2 - Camera.y - z, null);
				}else {
					g.drawImage(Entity.imageRightAxe, this.getX()+3 - Camera.x, this.getY()+2 - Camera.y - z, null);
				}
			}
			
			if(defesa && defendendo && shield.getLife()>0) {
				g.drawImage(Entity.imageShield, this.getX() - Camera.x, this.getY() - Camera.y - z + 2, null);
			}
		}
		
		else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if(arma) {
				//desemhar arma
				if(UseSword) {
					g.drawImage(leftPlayerAttack[indexAttack], this.getX() - Camera.x-6, this.getY() - Camera.y+4, null);
				}else if(!Axe){
					g.drawImage(Entity.WeaponSwordLeft, this.getX()-2 - Camera.x, this.getY()+1 - Camera.y - z, null);
				}
			}
			if(Axe) {
				if(UseAxe) {
					g.drawImage(leftPlayerAttackAxe[indexAttackAxe], this.getX() - 3 - Camera.x, this.getY() + 2 - Camera.y - z, null);
				}else {
					g.drawImage(Entity.imageLeftAxe, this.getX() - 3 - Camera.x, this.getY() + 2 - Camera.y - z, null);
				}
			}
			if(defesa && defendendo && shield.getLife()>0) {
				g.drawImage(Entity.imageShield, this.getX() - Camera.x, this.getY() - Camera.y - z + 2, null);
			}
			
		}
	}else {
		g.drawImage(PlayerDamage, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
	}
		if(isJumping) {
			g.setColor(new Color(0,0,0,120));
			g.fillOval(this.getX() - Camera.x + 3, this.getY() - Camera.y + 9, 8, 8);
		}
	}
}
