package light;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class Light extends PointSource{

	private float opacity = 0f, red = 0f, green = 0f, blue = 0f;
	private boolean follow = false, ignoreObjects = false, dontMix = false;
	private Timer scheduler;
	private String tag = "";
	private LightMap owner;
	
	public Light() {
		
		this(1f,1f,1f);
		
	}
	
	public Light(float r, float g, float b) {
		this(r,g,b,1f);
	}
	
	public Light(float r, float g, float b, float a) {
		
		this.setRed(r);
		this.setGreen(g);
		this.setBlue(b);
		this.setOpacity(a);
		this.setDistance(64);
		this.createUniqueID();
		
	}
	
	public int compareTo(Light other) {

		return this.getTag().compareTo(other.getTag());
		
	}
	
	public void update() {
		
		if(this.getTag().equals("mouselight") && MouseInfo.getPointerInfo() != null) {
			
			Point np = new Point((int) ((MouseInfo.getPointerInfo().getLocation().getX()) * (1/this.getOwner().getOwner().getGraphicsScale()) -this.getOwner().getOwner().getMidShiftX() +this.getOwner().getOwner().getFocusObject().getX()), 
							     (int) ((MouseInfo.getPointerInfo().getLocation().getY()) * (1/this.getOwner().getOwner().getGraphicsScale()) -this.getOwner().getOwner().getMidShiftY())+this.getOwner().getOwner().getFocusObject().getY());
			this.setFollowPoint(np);
			this.getOwner().getOwner().setApparentMouseX(np.x-2);
			this.getOwner().getOwner().setApparentMouseY(np.y-2);
			
		}
		if(follow) {
			
			this.setLocation(new Point(this.getFollowPoint().x,this.getFollowPoint().y));
			
		}
		
	}
	
	public void clearScheduler() {
		if (scheduler != null) {
			scheduler.cancel();
			scheduler.purge();
		}
	}
	
	public void cycle(float rate, long speed, float rangeMin, float rangeMax) {
		
		Light l = this;
		l.setSaturation(rangeMin - rate);
		clearScheduler();
		
		scheduler = new Timer();
		scheduler.schedule(new TimerTask() {

			@Override
			public void run() {

				if (l.isCycle()) {

					l.setSaturation(l.getSaturation() - rate);
					if (l.getSaturation() <= rangeMin) {
						l.setCycle(!l.isCycle());
					}

				} else if (!l.isCycle()) {

					l.setSaturation(l.getSaturation() + rate);
					if (l.getSaturation() >= rangeMax) {
						l.setCycle(!l.isCycle());
					}

				}
				
			}
			
		}, speed,speed);
		
	}
	
	public void cycleB(float rate, long speed, float rangeMin, float rangeMax) { // Useful for sun lights
		
		Light l = this;
		l.setSaturation(rangeMin - rate);
		
		clearScheduler();
		
		scheduler = new Timer();
		scheduler.schedule(new TimerTask() {

			@Override
			public void run() {

				if (l.isCycle()) {

					l.setSaturation(l.getSaturation() - rate);
					l.setStrength(l.getStrength() - rate/2);
					if (l.getSaturation() <= rangeMin) {
						l.setCycle(!l.isCycle());
					}

				} else if (!l.isCycle()) {

					l.setSaturation(l.getSaturation() + rate);
					l.setStrength(l.getStrength() + rate/2);
					if (l.getSaturation() >= rangeMax) {
						l.setCycle(!l.isCycle());
					}

				}
				
			}
			
		}, speed,speed);
		
	}
	
	public void follow(Point w) {
		
		follow = true;
		this.setLocation(w);
		
	}
	
	public void TestFall() {
		
		Light p = this;
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				
				p.setY((int) (p.getLocation().getY()+4));
				
			}
				
		}, 25, 25);
		
	}
	
	public Color getRenderColor(Point l) { // get color based on distance from light and the light's strength

		Color c = this.getColor();
		
		float ratio = (float) getLightRatio(l);
		if(ratio > 1f) {
			ratio = 1f;
		}
		
		int r = (int) (c.getRed() * ratio);
		int g = (int) (c.getGreen() * ratio);
		int b = (int) (c.getBlue() * ratio);
		
		c = new Color(r,g,b, 255 - (int)(255*ratio));
		
		return c;
		
	}
	
	public float getOpacityRatio(Point l) {
	
		return (float) (this.getOpacity()*this.getLightRatio(l));
		
	}
	
	public float getRedRatio(Point l) {
		
		return (float) (this.getRed()*this.getLightRatio(l));
		
	}
	
	public float getGreenRatio(Point l) {
		
		return (float) (this.getGreen()*this.getLightRatio(l));
		
	}
	
	public float getBlueRatio(Point l) {
		
		return (float) (this.getBlue()*this.getLightRatio(l));
		
	}
	
	public double getLightRatio(Point l) {
		
		Point p = this.getLocation();
		float distanceFrom = (float) l.distance(p);
		
		float full = (float) (255/(Math.pow(this.getDistance(), 2)) * Math.pow((distanceFrom - this.getDistance()),2));
		
		return full/255;  // y = 255/m^2 (x-m)^2
		
	}
	
	public float getImpact() {
		
		float i = (float) this.getStrength();
		
		
		
		return(i);
		
	}

	public Color getColor() {
		
		return new Color(red,green,blue,opacity);
		
	}
	
	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}
	
	public float getBlackness(Point l) { // find how dark a color is relative to a location
		
		float blackness = 0f;
		
		blackness += 1f - this.getRedRatio(l); // get avg difference between a color and full white light
		blackness += 1f - this.getGreenRatio(l); 
		blackness += 1f - this.getBlueRatio(l);
		//blackness /= 3;
		
		return blackness;
		
	}

	public float getOpacity() {
		return this.opacity;
	}

	public void setOpacity(float opacity) {
		if(opacity <= 1f && opacity >= 0f)
			this.opacity = opacity;
	}

	public boolean isFollow() {
		return follow;
	}

	public void setFollow(boolean follow) {
		this.follow = follow;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Timer getScheduler() {
		return scheduler;
	}

	public void setScheduler(Timer scheduler) {
		this.scheduler = scheduler;
	}

	public boolean isIgnoreObjects() {
		return ignoreObjects;
	}

	public void setIgnoreObjects(boolean ignoreObjects) {
		this.ignoreObjects = ignoreObjects;
	}

	public LightMap getOwner() {
		return owner;
	}

	public void setOwner(LightMap owner) {
		this.owner = owner;
	}

	public boolean isDontMix() {
		return dontMix;
	}

	public void setDontMix(boolean dontMix) {
		this.dontMix = dontMix;
	}
}
