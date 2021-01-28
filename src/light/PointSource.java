package light;

import java.awt.Point;

import engine.GraphicalObject;

public abstract class PointSource extends GraphicalObject{

	private boolean dynamic = true;
	private Point location;
	private float strength = 1f; // float between 0f - 1f
	private float saturation = 0.2f; // float between 0f - 1f
	private double distance;
	private boolean cycle;
	private Point followPoint;
	
	public PointSource() {
		super(-1);
		this.setLocation(new Point());
	}
	
	public void setX(int x) {

		super.setX(x);
		this.setLocation(new Point(x, this.getLocation().y));

	}

	public void setY(int y) {

		super.setX(y);
		this.setLocation(new Point(this.getLocation().x, y));

	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Point getFollowPoint() {
		return followPoint;
	}

	public void setFollowPoint(Point followPoint) {
		this.followPoint = followPoint;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public boolean isCycle() {
		return cycle;
	}

	public void setCycle(boolean cycle) {
		this.cycle = cycle;
	}
	
}
