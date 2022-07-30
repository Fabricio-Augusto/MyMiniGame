package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import entities.*;
import graficos.Spritesheet;
import main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public static BufferedImage minimapa;
	public static int[] minimapapixels;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			//Calcular os pixels do mapa
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			minimapa = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
			minimapapixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
			for(int xx = 0; xx<map.getWidth(); xx++) {
				for(int yy = 0; yy<map.getHeight(); yy++) {
					
					int pixelAtual = pixels[xx +(yy * map.getWidth())];
					if(Game.CUR_LEVEL==3) {
						tiles[xx+(yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_NIGHT);
					}else {
					tiles[xx+(yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					
					if(pixelAtual == 0xFF000000) {
						//Floor
						tiles[xx+(yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					} 
					else if(pixelAtual == 0xFF190B02) {
						tiles[xx+(yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_NIGHT);
					}
					else if(pixelAtual == 0xFFFFFFFF) {
						//Wall
						tiles[xx+(yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					} else if(pixelAtual == 0xFF267F00) {
						tiles[xx+(yy * WIDTH)] = new Mato(xx*16, yy*16, Tile.TILE_MATO);
					} 
					else if(pixelAtual == 0xFF070300) {
						tiles[xx+(yy * WIDTH)] = new Mato(xx*16, yy*16, Tile.TILE_MATO_NIGHT);
					}
					else if(pixelAtual == 0xFF4CFF00) {
						tiles[xx+(yy * WIDTH)] = new Arvore(xx*16, yy*16, Tile.TILE_ARVORE1);
					} else if(pixelAtual == 0xFFFF006E) {
						tiles[xx+(yy * WIDTH)] = new Arvore(xx*16, yy*16, Tile.TILE_ARVORE2);
				    } else if(pixelAtual == 0xFF7F006E) {
				    	tiles[xx+(yy * WIDTH)] = new Arvore(xx*16, yy*16, Tile.TILE_ARVORE3);
				    } else if(pixelAtual == 0xFF7F0037) {
				    	tiles[xx+(yy * WIDTH)] = new Arvore(xx*16, yy*16, Tile.TILE_ARVORE4);
				    } else if(pixelAtual == 0xFF7F6A00) {
						tiles[xx+(yy * WIDTH)] = new MineWall(xx*16, yy*16, Tile.TILE_MINI_WALL);
				    } else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					
					//Objs Game
					else if(pixelAtual == 0xFFFF0000){
						//Enemy
						Enemy enemyObj = new Enemy(xx*16, yy*16, 16, 16, 10, Entity.ENEMY_EN);
						Game.entities.add(enemyObj);
						Game.enemies.add(enemyObj);
					}  else if(pixelAtual == 0xFF000046) {
						Boss boss = new Boss(xx*16, yy*16,16,16,50,Entity.imageBoss);
						Game.entities.add(boss);
						Game.enemies.add(boss);
					}
					else if(pixelAtual == 0xFFFF7F7F){
						//Life Pack
						Lifepack lifePack = new Lifepack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN);
						//tiles[xx+(yy * WIDTH)] = new Mato(xx*16, yy*16, Tile.TILE_MATO);
						//lifePack.setMask(8, 8, 8, 8);
						Game.entities.add(lifePack);
						
					} else if(pixelAtual == 0xFF657C78) {
						Game.entities.add(new Sword(4, 25, xx*16, yy*16, 16, 16, Entity.WeaponSword));
					}else if(pixelAtual == 0xFF1E1E1E) {
						Game.entities.add(new Shield(100, xx*16, yy*16, 16, 16,Entity.imageShield));
					}
					else if(pixelAtual == 0xFFAF60FF) {
						Game.entities.add(new Axe(10, 60, xx*16, yy*16, 16, 16,Entity.imageAxe));
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext, int ynext, int zplayer) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		if(!(tiles[x1 + (y1*World.WIDTH)] instanceof WallTile || 
			 tiles[x2 + (y2*World.WIDTH)] instanceof WallTile || 
			 tiles[x3 + (y3*World.WIDTH)] instanceof WallTile || 
			 tiles[x4 + (y4*World.WIDTH)] instanceof WallTile ||
					tiles[x1 + (y1*World.WIDTH)] instanceof MineWall || 
					tiles[x2 + (y2*World.WIDTH)] instanceof MineWall || 
					tiles[x3 + (y3*World.WIDTH)] instanceof MineWall || 
					tiles[x4 + (y4*World.WIDTH)] instanceof MineWall)) {
			return true;
		}
		
		
		if(zplayer > 0) {
			if((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile || 
					tiles[x2 + (y2*World.WIDTH)] instanceof WallTile || 
					tiles[x3 + (y3*World.WIDTH)] instanceof WallTile || 
					tiles[x4 + (y4*World.WIDTH)] instanceof WallTile)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	public int numero_tiles() {
		int a = (WIDTH * HEIGHT);
		return a;
	}
	
	public void render(Graphics g) {
		
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void renderMiniMapa() {
		for(int i=0; i < minimapapixels.length; i++) {
			minimapapixels[i] = 0xFDF5E6;
		}
		for(int xx=0; xx < WIDTH; xx++) {
			for(int yy=0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile || tiles[xx + (yy*WIDTH)] instanceof MineWall) {
					minimapapixels[xx + (yy*WIDTH)] = 0x696969;
				}
			}
		}
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;
		minimapapixels[xPlayer + (yPlayer*WIDTH)] = 0x87CEEB;
	}
}
