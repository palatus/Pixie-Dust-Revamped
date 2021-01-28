package LostSpecies;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import engine.Backpack;
import engine.GraphicalObject;
import engine.Item;
import engine.Music;
import engine.Window;
import engine.WorldObject;
import light.Light;
import light.ParticleSpawner;

public class Starter {

	public static Backpack worldSpace = new Backpack(2048); // loads things into the scene later
	public static Window window = new Window("Lighting 2D");
	public static Scanner scanner;
	
	public static ArrayList<ArrayList<Boolean>> walls = new ArrayList<ArrayList<Boolean>>(); // data structure to store wall data for later construction
	public static Stack<Boolean> stackData = new Stack<Boolean>();
	
	public static void main(String[] args) {
		
		loadReservedIDList();
		
		//generateItems(150);
		generateMaze("maze");
		
		loadSceneItems();
		window.grabFocus();
		window.revalidate();
	
		window.setBackgroundURL("pbg.jpg");
		addTestLights();
		window.getLightmap().pushSunlight(window.getScreenSize());
		//displayWalls();
		
		scanner.close();
		
		//Music m = new Music("Music\\starg.wav");
		//m.play(15);
		
		//window.getLightmap().renderFireFlies();
		
		window.changeCursor("Images\\cursor2.png");
		
	}
	
	public static void displayWalls() {
		
		for(ArrayList<Boolean> w : walls) {
			
			for(Boolean b : w) {
				
				if(b)
					System.out.print("x");
				else
					System.out.print("o");
				
			}
			
		}
		
	}
	
	public static void generateMaze(String url) { // maze for final

		try {

			File f = new File("Maps\\" + url + ".map");
			scanner = new Scanner(f); // get each line
			int x = -32;
			int y = -32;
			int c = -1;
			
			while (scanner.hasNextLine()) {
				
				c++;
				
				if(walls.size() <= c) {
					walls.add(new ArrayList<>());
				}
				
				y += 32;
				x = -32;
				
				String line = scanner.nextLine();

				Scanner inner = new Scanner(line); // scan each character
				
				for(int j = 0; j<line.length(); j++) {
					
					if(walls.get(c).size() <= j) {
						walls.get(c).add(false);
					}
					
					x+=32;
					
					if (line.charAt(j) == 'S' || line.charAt(j) == 'E') {
						Light l2 = new Light();
						l2.setX(x+16);
						l2.setY(y+16);
						l2.setRed(1f);
						l2.setGreen(0.6f);
						l2.setBlue(0.4f);
						l2.setDynamic(false);
						l2.setDistance(256);
						l2.setStrength(0.6f);
						l2.setSaturation(1.0f);
						l2.cycleB(0.08f, 100, 0.8f, 1.0f);
						window.getLightmap().getLights().add(l2);
						l2.setTag("EndStartLight");
					}
					
					if (line.charAt(j) == 'L') {
						Light l2 = new Light();
						l2.setX(x+22);
						l2.setY(y+18);
						l2.setRed(1f);
						l2.setGreen(0f);
						l2.setBlue(0f);
						l2.setDynamic(false);
						l2.setDistance(512);
						l2.setStrength(0.5f);
						l2.setSaturation(0.7f);
						l2.cycleB(0.025f, 100, 0, 1.5f);
						window.getLightmap().getLights().add(l2);
						l2.setTag("redlight");
						
						Item i = new Item("redlight");
						i.setX(x+20);
						i.setY(y+16);
						i.setHeight(32);
						i.setWidth(32);
						i.setScale(1);
						i.setImageUrl("rock.png");
						i.setCastsShadows(false);
						worldSpace.addItem(i);
						
					}
					
					if (line.charAt(j) == 'R') {
						
						Item i = new Item("EasterEgg");
						i.setX(x+16);
						i.setY(y+16);
						i.setHeight(32);
						i.setWidth(32);
						i.setScale(2);
						i.setImageUrl("egg1.png");
						i.setCastsShadows(false);
						worldSpace.addItem(i);
						
					}
					if (line.charAt(j) == 'Z') {
						
						Item i = new Item("EasterEgg");
						i.setX(x+8);
						i.setY(y+8);
						i.setHeight(16);
						i.setWidth(16);
						i.setScale(1);
						i.setImageUrl("egg2.png");
						i.setCastsShadows(false);
						worldSpace.addItem(i);
						
					}
					
					if (line.charAt(j) == 'x') {
						Item i = new Item("Wall");
						i.setX(x);
						i.setY(y);
						i.setHeight(32);
						i.setWidth(32);
						i.setScale(1);
						i.setImageUrl("gravel.png");
						i.setCastsShadows(true);
						worldSpace.addItem(i);
						walls.get(c).set(j,true); 
						
					} else {
						walls.get(c).set(j,false); 
					}
					
				}
				
				
				inner.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public static void addTestLights() {
		
//		Light l = new Light();
//		l.setLocation(new Point(512,256));
//		l.setDistance(1024);
//		l.setGreen(0f);
//		l.setBlue(0f);
//		l.setStrength(0.5f);
//		l.setSaturation(0.3f);
//		window.getLightmap().getLights().add(l);
//		
//		Light l2 = new Light();
//		l2.setLocation(new Point(352,352));
//		l2.setRed(0f);
//		l2.setGreen(1f);
//		l2.setBlue(0f);
//		l2.setDynamic(false);
//		l2.setDistance(512);
//		l2.setStrength(1f);
//		l2.setSaturation(0.3f);
//		window.getLightmap().getLights().add(l2);
//		
//		l.setDynamic(false);
		//l.TestFall();
		
		window.getLightmap().pushMouseLight();
		
	}
	
	public static void loadSceneItems() {
		
		for(GraphicalObject i: worldSpace.getContents()) {
			
			window.getScene().pushSceneObject(i);
			
		}
		
	}
	
	public static void loadReservedIDList() {
		
		// TODO read from file stuff
		
		Item i = new Item("Reserved ID");
		i.setOwnerGroup(worldSpace);
		i.setId(23);
		WorldObject.ReservedID.add(i);
		
	}

}
