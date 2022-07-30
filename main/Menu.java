package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import world.World;

public class Menu {
	
	public String[] options = {"novo jogo", "carregar jogo", "sair", "comandos"};
	
	public int curOption = 0;
	public int maxOption = options.length - 1;
	public boolean comandosAparecer=false;
	public boolean up, down, enter;
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up && comandosAparecer==false) {
			up = false;
			curOption--;
			if(curOption < 0) {
				curOption = maxOption;
			}
		}
		if(down && comandosAparecer==false) {
			down = false;
			curOption++;
			if(curOption > maxOption) {
				curOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[curOption] == "novo jogo") {
				Game.gameState = "NORMAL";
				pause=false;
				file = new File("save.txt");
				file.delete();
			}else if(options[curOption] == "carregar jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}else if(options[curOption] == "sair") {
				System.exit(1);
			}else if(options[curOption] == "comandos") {
			   comandosAparecer=true;
			}
		}
		
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i=0; i<spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level":
				int level=1;
				try {
					level = Integer.parseInt(spl2[1]);
				}catch(Exception e) {}
				Game.CUR_LEVEL= level;
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i=0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				}catch(Exception e) {}
			}catch(FileNotFoundException e) {}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i=0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n=0; n < value.length; n++) {
				//cripitografa os arquivos
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length -1)
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("My First Game", 240, 80);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if(comandosAparecer==false) {
			if(pause == false)
				g.drawString("Novo Jogo", 300, 160);
			else
				g.drawString("resumir", 320, 160);
			g.drawString("Carregar Jogo", 280, 240);
			g.drawString("Sair", 340, 320);
			g.drawString("Comandos", 310, 400);
			
			if(options[curOption] == "novo jogo") {
				if(pause == false)
					g.drawString(">", 300 - 20, 160);
				else
					g.drawString(">", 320 - 20, 160);
			}else if(options[curOption] == "carregar jogo") {
				g.drawString(">", 280 - 20, 240);
			}else if(options[curOption] == "sair") {
				g.drawString(">", 340 - 20, 320);
			}
			else if(options[curOption] == "comandos") {
				g.drawString(">", 310 - 20, 400);
			}
		}else {
			g.drawString("W, A, S, D para mover", 200, 130);
			g.drawString("Q levanta e abaixa o escudo", 200, 170);
			g.drawString("L para salvar", 200, 220);
			g.drawString("M desliga e liga o minimapa", 200, 270);
			g.drawString("Mouse ataca com a arma", 200, 320);
			g.drawString("Espace para pular", 200, 370);
			g.drawString("1 e 2 troca de armas", 200, 420);
			g.drawString("Z para voltar ao menu principal", 200, 460);
		}
	}
}
