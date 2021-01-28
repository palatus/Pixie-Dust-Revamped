package light;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public class Particle extends Light{ // essentially a light with velocity and an optional image display

	private ImageIcon particleImage = new ImageIcon("");
	private float hSpeed = 0, vSpeed = 0;
	private float mass = 1;
	private ArrayList<Runnable> functions = new ArrayList<Runnable>();
	private long updateSpeed = 20, lifeTime = 1000;
	private boolean processing = true;
	
	public Particle() {
		this(1f,1f,1f);
	}
	
	public Particle(float r, float g, float b) {
		
		super(r,g,b);
		this.setWidth(2);
		this.setHeight(2);
		this.setScheduler(new Timer());
		
		this.getScheduler().schedule(new TimerTask() {

			@Override
			public void run() {
				
				if(processing) {
					
					for(Runnable r : functions) {
						r.run();
					}
					
				}
				
			}
			
		}, updateSpeed, updateSpeed);
		
	}
	
	public void enableFade() {
	
		this.getFunctions().add(new Runnable() {

			@Override
			public void run() {
				
				setLifeTime(getLifeTime()-updateSpeed);
				
				if(getSaturation()-getSaturation()/(getLifeTime()/100) >= 0) {
					setSaturation(getSaturation()-(getSaturation()/(getLifeTime()/100)));
				} else {
					setSaturation(0);
				}
				
				
				if(getSaturation() < 0.02f) {
					getOwner().removeLight(getId());
				}
				
			}
			
		});
		
	}
	
	public void enableFireFly() {
		
		final float part = getSaturation()/(getLifeTime()/400);
		this.getFunctions().add(new Runnable() {

			@Override
			public void run() {
				
				setLifeTime(getLifeTime()-updateSpeed);
				
				if(getSaturation()-getSaturation()/(getLifeTime()/400) >= part) { // fade in
					
					setSaturation(getSaturation()-(getSaturation()/(getLifeTime()/800)));
					
				} else if(getSaturation()-getSaturation()/(getLifeTime()/100) >= 0) { // fade out
					
					setSaturation(getSaturation()-(getSaturation()/(getLifeTime()/400)));
					
				} else {
					
					setSaturation(0);
					
				}

				if (getSaturation() < 0.02f) {
					getOwner().removeLight(getId());
				}
				
			}
			
		});
		
	}
	
	public void enableWind(int direction, float strength) { // top - right - bottom - left
											   				   //  0  -	  1   -   2    -   3
		this.getFunctions().add(new Runnable() {

			@Override
			public void run() {
				
				if(direction == 0 || direction == 2) {
					if(direction == 0)
						setLocation(new Point(getLocation().x,(int) (getLocation().y - strength)));
					else
						setLocation(new Point(getLocation().x,(int) (getLocation().y + strength)));
				} else {
					if(direction == 1)
						setLocation(new Point((int) (getLocation().x + strength),getLocation().y));
					else
						setLocation(new Point((int) (getLocation().x - strength),getLocation().y));
				}
				
			}
			
		});
		
	}

	public ImageIcon getParticleImage() {
		return particleImage;
	}

	public void setParticleImage(ImageIcon particleImage) {
		this.particleImage = particleImage;
	}

	public float gethSpeed() {
		return hSpeed;
	}

	public void sethSpeed(float hSpeed) {
		this.hSpeed = hSpeed;
	}

	public float getvSpeed() {
		return vSpeed;
	}

	public void setvSpeed(float vSpeed) {
		this.vSpeed = vSpeed;
	}

	public ArrayList<Runnable> getFunctions() {
		return functions;
	}

	public void setFunctions(ArrayList<Runnable> functions) {
		this.functions = functions;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public long getUpdateSpeed() {
		return updateSpeed;
	}

	public void setUpdateSpeed(long updateSpeed) {
		this.updateSpeed = updateSpeed;
	}

	public boolean isProcessing() {
		return processing;
	}

	public void setProcessing(boolean processing) {
		this.processing = processing;
	}

	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}
	
}
