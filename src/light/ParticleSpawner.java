package light;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;

import engine.GraphicalObject;
import engine.Scene;

public class ParticleSpawner extends GraphicalObject{

	private Point location = new Point(0,0);
	private Timer scheduler;
	private Scene owner;
	private ArrayList<Particle> contents = new ArrayList<Particle>();
	
	public ParticleSpawner(Scene s) {
		this.setOwner(s);
	}
	
	public void emitSingle(int dir, float speed) {
		
		int particleRandom = (int) (Math.random() * this.getContents().size());
		Particle p = contents.get(particleRandom);
		
		p.setLocation(this.getLocation());
		p.setX(this.getX()+this.getWidth()/2);
		p.setY(this.getY()+this.getHeight()/2);
		p.enableWind(dir, speed);
		
		this.getOwner().getLightmap().addLight(p);
		
	}
	
	public void emitSingleRandom(float speed) {
		
		int particleRandom = (int) (Math.random() * this.getContents().size());
		Particle p = contents.get(particleRandom);
		
		p.setLocation(this.getLocation());
		p.setX(this.getX()+this.getWidth()/2);
		p.setY(this.getY()+this.getHeight()/2);
		p.enableWind((int) (Math.random()*4), speed);
		
		this.getOwner().getLightmap().addLight(p);
		
	}
	
	public void emitN(int dir, int speed, int n) {
		
		for(int i = 0; i<n; i++)
			emitSingle(dir, speed);
		
	}
	
	public void emitNRandom(int speed, int n) {
		
		for(int i = 0; i<n; i++)
			emitSingleRandom(speed);
		
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Timer getScheduler() {
		return scheduler;
	}

	public void setScheduler(Timer scheduler) {
		this.scheduler = scheduler;
	}

	public ArrayList<Particle> getContents() {
		return contents;
	}

	public void setContents(ArrayList<Particle> contents) {
		this.contents = contents;
	}

	public Scene getOwner() {
		return owner;
	}

	public void setOwner(Scene owner) {
		this.owner = owner;
	}
	
}
