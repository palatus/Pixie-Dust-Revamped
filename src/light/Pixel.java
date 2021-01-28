package light;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import engine.GraphicalObject;

public class Pixel extends PointSource{

	private double sizeSquared = 4; // how many real pixels this 'pixel' represents
	private boolean on = true; // Black, or lit
	private LightMap owner;
	private Color renderColor;
	private Color baseColor;

	public Pixel(Point p) {

		super();
		this.setLocation(p);
		this.setX((int) p.getX());
		this.setY((int) p.getY());
		this.setBaseColor(new Color(0f,0f,0f,0f)); 

	}
	
	public double getWidthPrecise() {
		
		return sizeSquared;
		
	}
	
	public double getHeightPrecise() {
		
		return sizeSquared;
		
	}
	
	public boolean isOnShadowCaster(ArrayList<GraphicalObject> op) {
		
		for (int i = 0; i < op.size(); i++) {
			
			if (op.get(i).CastsShadows()) {
				Rectangle2D r = new Rectangle2D.Double(op.get(i).getX(), op.get(i).getY(),
						op.get(i).getEffectiveWidth(), op.get(i).getEffectiveHeight());
				Rectangle2D r2 = new Rectangle2D.Double(this.getRealLocation().getX(), this.getRealLocation().getY(),
						this.getWidth() * this.getSizeSquared(), this.getHeight() * this.getSizeSquared());

				if (r.intersects(r2)) {
					return true;
				}
			}
			
		}
		
		return false;
		
	}
	
	public boolean isObstructed(Light light, ArrayList<GraphicalObject> op, Graphics2D g) { // Do we mix the light onto the pixel, or not

		if(light.isIgnoreObjects()) { // This light does not give a shit!
			return false;
		}
//		if(this.isOnShadowCaster(op)) {
//			return false;
//		}
		
		Line2D l = new Line2D.Double(light.getLocation().getX(), light.getLocation().getY(), // "draw" line from the light source to the pixel
				this.getRealLocation().getX()+(this.getWidth()*this.getSizeSquared())/2, this.getRealLocation().getY()+(this.getHeight()*this.getSizeSquared())/2);

		boolean close = false;
		for (int i = 0; i < op.size(); i++) {

			Rectangle2D r = new Rectangle2D.Double(op.get(i).getX(), op.get(i).getY(), op.get(i).getEffectiveWidth(),op.get(i).getEffectiveHeight()); // object bounds
			
			Rectangle2D r2 = new Rectangle2D.Double(this.getRealLocation().getX(), this.getRealLocation().getY(),
					this.getWidth()*this.getSizeSquared(), this.getHeight()*this.getSizeSquared()); // pixel object bounds
			
			if(r2.intersects(r)) { // if the pixel is over the object, then mix anyway
				//op.get(i).setCastingShadow(false);
				return false;
			}
			
			if (l.intersects(r) && op.get(i).CastsShadows()) { // if the line from the light to the pixel is obstructed by a graphical object, then don't mix that light
				op.get(i).setCastingShadow(true);
				return true;
			}
			
		}

		// Quick, but ignores layer sorting
		if(owner.isShowRays() && this.getRealLocation().distance(light.getLocation()) <= light.getDistance())
			g.draw(l);

		// mix the light
		return false;
		
	}

	public void calculateColor(Object[] l, ArrayList<GraphicalObject> sceneContents, Graphics2D g) {

		Light[] lights = null;
		boolean iosc = this.isOnShadowCaster(sceneContents);

		if(iosc){

			lights = new Light[1];
				Light s = owner.getTaggedLight("Sun");
			if(s != null) {
				lights[0] = s;
			} else {

			}

		} else {
			lights = (Light[])l;
		}

		Color blended = baseColor;
		Point p = this.getRealLocation();
		int affected = 0;
		float totalStrength = 0;
		
		if(this.getRealLocation() != null)
		for (int i = 0; i < lights.length; i++) {
			
			if(lights[i] != null) {

				boolean obs = isObstructed(lights[i], sceneContents, g);
				if (!iosc && obs && (!lights[i].getTag().equalsIgnoreCase("sun"))) {
					continue;
				}

				double distance = this.getRealLocation().distance(lights[i].getLocation()); // distance from this to light
				//g.fillRect(lights[i].getLocation().x, lights[i].getLocation().y, 32, 32);

				if (distance <= lights[i].getDistance()) { // within distance & is in light path

					float alphaStage = (float) lights[i].getLightRatio(p)*lights[i].getStrength();

					alphaStage *= lights[i].getSaturation();

					if(alphaStage > 1f) {
						alphaStage = 1f;
					}
					if(alphaStage < 0f) {
						alphaStage = 0f;
					}

					if(iosc) {
						alphaStage = lights[i].getSaturation();
					} else if(lights[i].isDontMix() && !obs){
						alphaStage = lights[i].getSaturation();
					}

					blended = Colors.blend(blended, lights[i].getRenderColor(p), alphaStage);
					affected++;
					totalStrength += lights[i].getStrength()*lights[i].getLightRatio(p);

				}
			}
		}
		
		int alphaStage = (int) (Colors.getBlackness(blended) -totalStrength*33 + 32);
		
		if(alphaStage > 255) {
			alphaStage = 255;
		}
		if(alphaStage < 0) {
			alphaStage = 0;
		}
		
		blended = new Color(blended.getRed(), blended.getGreen(), blended.getBlue(), alphaStage);
		
		if(affected == 0) {
			blended = new Color(0f,0f,0f,1f);
		}
		
		
		renderColor = blended;
		
	}
	
	public void calculateColor(ArrayList<Light> lights, ArrayList<GraphicalObject> sceneContents ,Graphics2D g) {
		calculateColor(lights.toArray(new Light[lights.size()]), sceneContents ,g);
	}
	
	public Point getRealLocation() { // need to calculate it's location when the camera moves
		
		Point shifted = getLocation();
		
		int mx = shifted.x + this.getOwner().getOwner().getShiftX();
		int my = shifted.y + this.getOwner().getOwner().getShiftY();
		
		shifted = new Point(mx,my);
		
		
		return shifted;
		
	}

	public double getSizeSquared() {
		return sizeSquared;
	}

	public void setSizeSquared(double sizeSquared) {
		this.sizeSquared = sizeSquared;
	}

	public boolean ison() {
		return on;
	}

	public void seton(boolean on) {
		this.on = on;
	}

	public Color getRenderColor() {
		return renderColor;
	}

	public void setRenderColor(Color renderColor) {
		this.renderColor = renderColor;
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public LightMap getOwner() {
		return owner;
	}

	public void setOwner(LightMap owner) {
		this.owner = owner;
	}
	
}
