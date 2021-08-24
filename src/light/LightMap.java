package light;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import engine.Window;

import java.awt.Point;

public class LightMap {

	private ArrayList<Pixel> litPixels = new ArrayList<Pixel>();
	private ArrayList<Light> lights = new ArrayList<Light>();
	private Window owner;
	private float wallCoverage = 0.6f;
	private boolean updateReady = false;
	private Timer runner = new Timer();
	private int threadFinishCount = 0;

	private boolean showRays = false;

	public LightMap() {
		
	}
	
	public LightMap(Window w) {
		this();
		owner = w;
	}
	
	public void recreate(Dimension di, double psize) { // method to return a standard LightMap based on 2 parameters.
		
		this.litPixels = new ArrayList<Pixel>();

		double scale = psize; // size of each pixel
		
		Dimension d = new Dimension((int)(di.getWidth() * 1/psize), (int)(di.getHeight() * 1/psize));
		
		for(int i = -2; i<=d.getWidth(); i++) {
			
			for(int j = -2; j<=d.getHeight(); j++) { // double loop - create pixels based on screen size
				
				Pixel p = new Pixel(new Point((int)(i*scale),(int)(j*scale)));
				p.setSizeSquared(scale);
				p.setOwner(this);
				litPixels.add(p);
				
			}
			
		}
		
	}
	
	public void listLights() {
		
		for(Light l : this.getLights()) {
			System.out.println(l.getTag());
		}
		
	}
	
	public static LightMap create(Dimension di, double psize, Window o) { // method to return a standard LightMap based on 2 parameters.
		
		LightMap l = new LightMap();
		l.setOwner(o);
		
		double scale = psize; // size of each pixel
		
		Dimension d = new Dimension((int)(di.getWidth() * 1/psize), (int)(di.getHeight() * 1/psize));
		
		for(int i = -2; i<d.getWidth(); i++) {
			
			for(int j = -2; j<=d.getHeight(); j++) { // double loop - create pixels based on screen size
				
				Pixel p = new Pixel(new Point((int)(i*scale),(int)(j*scale)));
				p.setSizeSquared(scale);
				p.setOwner(l);
				l.litPixels.add(p);
				
			}
			
		}
		
		return l;
		
	}
	
	public Light getTaggedLight(String tag) {
		
		Light l = null;
		for(int i = 0; i<this.getLights().size(); i++) {
			
			if(this.getLights().get(i).getTag().equalsIgnoreCase(tag)) {
				l = this.getLights().get(i);
			}
			
		}
			
		return l;
		
	}
	
	public void pushSunlight(Dimension d) {
		
		Light l = new Light();
		Point midScreen = new Point((int) (d.getWidth()/2),(int) (d.getHeight()/2));
		l.setLocation(midScreen);
		l.setFollow(true);
		l.setFollowPoint(midScreen);
		l.setRed(0.6f);
		l.setGreen(0.55f);
		l.setBlue(0.5f);
		l.setStrength(0.7f);
		l.setDontMix(true);
		l.setDistance(Math.pow(d.getWidth(),2));
		l.setTag("Sun");
		
		l.cycleB(0.00002f, 30, 0.15f, 0.3f);
		l.setIgnoreObjects(true);
		
		addLight(l);
		
	}
	
	public void pushAmbientLight(Dimension d) {
		
		Light l = new Light();
		Point midScreen = new Point((int) (d.getWidth()/2),(int) (d.getHeight()/2));
		l.setLocation(midScreen);
		l.setFollow(true);
		l.setFollowPoint(midScreen);
		l.setGreen(1f);
		l.setRed(1f);
		l.setBlue(1f);
		l.setStrength(0.7f);
		l.setDistance(Math.pow(d.getWidth(),2));
		l.setTag("Sun");

		l.setIgnoreObjects(true);
		
		addLight(l);
		
	}
	
	public void pushMouseLight() {
		
		Light l = new Light();
		l.setFollow(true);
		l.setFollowPoint(MouseInfo.getPointerInfo().getLocation()); 
		l.setTag("mouselight");
		l.setStrength(0.7f);
		l.setDistance(256);
		l.setBlue(1f);
		l.setGreen(0.5f);
		l.setRed(0.5f);
		l.setSaturation(0.8f);
		addLight(l);
		
	}
	
	public void addLight(Light l) {
		
		l.setOwner(this);
		this.getLights().add(l);
		
	}
	
	public void updateLights() { // update each Light

		try {
			for (int i = 0; i < lights.size(); i++) {

				Light l = lights.get(i);
				if (l != null) {
					l.update();

					if (l instanceof Particle) {
						Particle p = (Particle) l;
						Color pc = new Color(l.getRed(), l.getGreen(), l.getBlue());
						this.getOwner().getGraphics().setColor(pc);
						this.getOwner().getGraphics().fillRect(p.getX(), p.getY(), p.getEffectiveWidth(),
								p.getEffectiveHeight());
					}
				}

			}
		} catch (IndexOutOfBoundsException e){

		}
		
	}

	public void loadPixels(ArrayList<Pixel> p) {
		
		this.setLitPixels(p);
		
	}
	
	public void loadPixels(Pixel[] p) {
		
		for(int i = 0; i<p.length; i++) {
			litPixels.add(p[i]);
		}
		
	}
	
	public ArrayList<Pixel> getLitPixels() {
		return litPixels;
	}

	public void setLitPixels(ArrayList<Pixel> litPixels) {
		this.litPixels = litPixels;
	}

	public ArrayList<Light> getLights() {
		return lights;
	}

	public void setLights(ArrayList<Light> lights) {
		this.lights = lights;
	}

	public Window getOwner() {
		return owner;
	}

	public void setOwner(Window owner) {
		this.owner = owner;
	}

	public float getWallCoverage() {
		return wallCoverage;
	}

	public void setWallCoverage(float wallCoverage) {
		this.wallCoverage = wallCoverage;
	}

	public boolean isUpdateReady() {
		return updateReady;
	}

	public void setUpdateReady(boolean updateReady) {
		this.updateReady = updateReady;
	}

	public Timer getRunner() {
		return runner;
	}

	public void setRunner(Timer runner) {
		this.runner = runner;
	}

	public int getThreadFinishCount() {
		return threadFinishCount;
	}

	public void setThreadFinishCount(int threadFinishCount) {
		this.threadFinishCount = threadFinishCount;
	}
	
	public void renderFireFlies() {
		
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				
				Particle p = new Particle(0.6f, 1f, 0.1f);
				p.setDistance(32);
				p.setLifeTime(8000);
				p.setTag("test pixel");
				p.setSaturation(0.8f);
				p.setStrength(0.2f);
				
				p.setX((int) ((Math.random() * 1920) / getOwner().getGraphicsScale()));
				p.setY((int) ((Math.random() * 1080) / getOwner().getGraphicsScale()));
				
				addLight(p);
				p.enableWind((int) (Math.random()*4), (float) (Math.random()*3));
				p.enableFireFly();
				
			}
			
		}, 50,50);
		
	}

	public boolean removeLight(int id) {

		try {
			for (int i = 0; i < this.getLights().size(); i++) {

				if (this.getLights().get(i) != null)
					if (this.getLights().get(i).getId() == id) {
						this.getLights().remove(i);
						return true;
					}

			}

			return false;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}

	public Light getFirstLightWithTag(String tag){

		for (Light l: this.getLights()) {
			if(l.getTag().equalsIgnoreCase(tag)){
				return l;
			}
		}

		return null;

	}

	public boolean showingRays() {
		return showRays;
	}

	public void setShowRays(boolean showRays) {
		this.showRays = showRays;
	}
}
