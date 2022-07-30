package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import graficos.Spritesheet;
import graficos.UI;
import world.World;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;

	public static JFrame frame;
	private boolean isRunning = true;
	private Thread thread;
	public static final int WIDTH = 240, HEIGHT = 160, SCALE = 3;
	public boolean jasalvei=false;

	private BufferedImage image;
	private Graphics g;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random random;
	public UI ui;

	public static int CUR_LEVEL=1;
	private static int MAX_LEVEL=3;
	
	public static boolean musicaLigada=true;
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	private boolean mapaVisivel = true;
	public Menu menu;
	public boolean saveGame = false;
	public int mx, my;
	public static int[] pixels;
	public static int[] lightMapPixels;
	public BufferedImage lightmap;

	public Game() {
		random = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels,0, lightmap.getWidth());
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		menu = new Menu();
		
	}

	// Criação da Janela
	public void initFrame() {
		frame = new JFrame("projeto1");
		frame.add(this);
		frame.setResizable(false);// Usuário não irá ajustar janela
		frame.pack();
		frame.setLocationRelativeTo(null);// Janela inicializa no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Fechar o programa por completo
		frame.setVisible(true);// Dizer que estará visível
	}

	// Threads
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Método Principal
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	//Ticks do Jogo
	public void tick() {
		/*if(CUR_LEVEL==3) {
			Sound.music.stop();
			Sound.musicLevel3.loop();
		}else {
			Sound.musicLevel3.stop();
			Sound.music.loop();
		}*/
		if(gameState == "NORMAL") {
			if(saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.CUR_LEVEL};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo Salvo!");
				if(CUR_LEVEL == 2) {
					jasalvei=true;
				}else
					jasalvei=false;
			}
			this.restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			if(enemies.size() == 0) {
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level"+CUR_LEVEL+".png";
				world.restartGame(newWorld);
			}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 25) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) {
					this.showMessageGameOver = false;
				}else
					this.showMessageGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				world.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
	}
	
	
	public void AplicarLuz() {
		for(int xx=0; xx< Game.WIDTH; xx++) {
			for(int yy=0; yy< Game.HEIGHT; yy++) {
				if(lightMapPixels[xx + (yy * Game.WIDTH)] == 0xffffffff) {
					pixels[xx + (yy * Game.WIDTH)] = 0;
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		} 

		g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

		Collections.sort(entities, Entity.nodeSorter);
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);

			e.render(g);
		}
		if(CUR_LEVEL==3) {
			AplicarLuz();
		}
		ui.render(g);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font("arial", Font.BOLD,18));
		if(CUR_LEVEL!=3)
			g.setColor(Color.black);
		else
			g.setColor(Color.white);
		if(player.getShield()!=null) {
			if(player.getShield().getLife()>0)
				g.drawString("Life Shield: "+ player.getShield().getLife(), 570, 20);
			else
				g.drawString("Life Shield: "+ 0, 570, 20);
		}
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD,28));
			g.setColor(Color.WHITE);
			g.drawString("Game Over", (WIDTH*SCALE)/2 - 80, (HEIGHT*SCALE)/2 + 10);
			if(showMessageGameOver) {
				g.drawString("Pressione Enter para reiniciar", (WIDTH*SCALE)/2 - 190, (HEIGHT*SCALE)/2 + 45);
			}
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		World.renderMiniMapa();
		if(CUR_LEVEL==1 && mapaVisivel) {
			g.drawImage(world.minimapa, 600, 360, world.WIDTH*5, world.HEIGHT*5, null);
		}
		if(CUR_LEVEL==2 && mapaVisivel) {
			g.drawImage(world.minimapa, 550, 300, world.WIDTH*3, world.HEIGHT*3, null);
		}
		bs.show();
	}

	// Controle de FPS
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();// Usa o tempo atual do computador em nano segundos, bem mais preciso
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;// Calculo exato de Ticks
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		//requestFocus();
		// Ruuner Game
		while (isRunning == true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				//delta--;
				delta=0;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		
		// Esquerda e Direita
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {

			//System.out.println("Direita");
			player.right = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {

			//System.out.println("Esquerda");
			player.left = true;

		}

		// Cima e Baixo
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {

			//System.out.println("Cima");
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//System.out.println("Baixo");
			player.down = true;
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
				
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_L) {
			if(gameState == "NORMAL" && jasalvei == false) {
				this.saveGame = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_M) {
			if(gameState == "NORMAL") {
				if(mapaVisivel==true) {
					mapaVisivel=false;
				}else
					mapaVisivel=true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			if(gameState == "MENU") {
				menu.comandosAparecer=false;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_Q) {
			if(player.getShield()!=null) {
				player.Axe = false;
				if(player.defendendo) {
					player.defendendo=false;
				}else {
					player.defendendo=true;
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_1) {
			if(player.getSword() != null) {
				if(player.getArma()==false) {
					player.Axe=false;
					player.setArma(true);
				}else {
					player.setArma(false);
				}
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			if(player.getAxe() != null) {
				if(player.Axe==false) {
					player.defendendo=false;
					player.setArma(false);
					player.Axe=true;
				}else {
					player.Axe=false;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Esquerda e Direita
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {

			//System.out.println("Direita Solto");
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {

			//System.out.println("Esquerda Solto");
			player.left = false;

		}

		// Cima e Baixo
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {

			//System.out.println("Cima Solto");
			player.up = false;

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//System.out.println("Baixo Solto");
			player.down = false	;

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(player.getSword() != null && player.getArma() && player.getSword().getcanUse()) { 
			player.getSword().setUse(true);
			player.getSword().setcanUse(false);
			player.UseSword=true;
		}else if(player.getAxe() != null && player.Axe && player.getAxe().getcanUse()) {
			player.getAxe().setUse(true);
			player.getAxe().setcanUse(false);
			player.UseAxe=true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}

}
