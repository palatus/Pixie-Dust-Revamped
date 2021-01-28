package engine;

import java.awt.Point;

public class GraphicalObject implements WorldObject{

	private int x = 0, y = 0;
	private int scale = 1;
	private int width = 1, height = 1;
	private int id = -1;
	private Backpack ownerGroup;
	private boolean castsShadows = false, castingShadow = true, render = true;
	private String imageUrl;
	
	public GraphicalObject() {
		this.createUniqueID();
	}
	
	public GraphicalObject(int id) {
		this.id = id;
	}

	public Point getLocation() {
		return new Point(this.getX(),this.getY());
	}
	
	public double distanceFrom(GraphicalObject g) {
		
		double d = 0;
		
		Point p = new Point(this.getX(),this.getY());
		Point p2 = new Point(g.getX(),g.getY());
		
		d = p.distance(p2);
		
		return d;
		
	}
	
	public int createUniqueID() { // generate a unique object id

		if(ListedID.size() == 0) { // Load the reserved ID list before assigning any usable ID numbers (must be unloaded before a change can be set)
			initReserved();
		}
		
		int id = -1;
		do {
			id++;
		} while (checkID(id) || checkReservedList(id));
		
		this.setId(id);
		ListedID.add(this); // push id into the list
		return id;

	};
	
	public void bindToNearestInt(int b){ // set the x and y values to fit to the nearest multiple of the given integer.
		
		this.setX(b*(Math.round(this.getX()/b)));
		this.setY(b*(Math.round(this.getY()/b)));
		
	}
	
	public int getX() {
		return x;
	}

	public int getEffectiveWidth() {
		return this.getWidth()*this.getScale();
	}
	
	public int getEffectiveHeight() {
		return this.getHeight()*this.getScale();
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getId() {
		return id;
	}
	
	public String getKey() {
		
		return ""+this.getId();
		
	}

	public boolean setId(int id) {
		
		if(!checkID(id) && !checkReservedList(id)) {
			this.id = id;
			return true;
		} else {
			//System.err.println("The ID of "+id+" cannot be assigned.");
			return false;
		}
		
	}

	@Override
	public void shift(int hor, int vert) {

		this.setX(this.getX() + hor);
		this.setY(this.getY() + vert);
		
	}

	@Override
	public void stretch(int width, int height) {
		
		this.setWidth(width);
		this.setHeight(height);
		
	}

	@Override
	public int compareTo(WorldObject o) {
		
		if (o instanceof GraphicalObject) {

			GraphicalObject g = (GraphicalObject) o;
			
			System.out.println(this.getId()+" - "+g.getId());
			
			if (this.getId() == g.getId()) {
				System.out.println("equal");
				return 0;
			}
			
			if (this.getId() > g.getId()) {
				System.out.println("greater");
				return 1;
			} else if (this.getId() < g.getId()){
				System.out.println("Less");
				return -1;
			}

		}
		
		return 0;
	}

	@Override
	public boolean equals(WorldObject o) {
		
		if (o instanceof GraphicalObject) {

			GraphicalObject g = (GraphicalObject) o;
			
			if (this.getId() == g.getId()) {
				return true;
			}

		}
		
		return false;
	}

	public Backpack getOwnerGroup() {
		return ownerGroup;
	}

	public void setOwnerGroup(Backpack ownerGroup) {
		this.ownerGroup = ownerGroup;
	}

	public boolean CastsShadows() {
		return castsShadows;
	}

	public void setCastsShadows(boolean castsShadows) {
		this.castsShadows = castsShadows;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isCastingShadow() {
		return castingShadow;
	}

	public void setCastingShadow(boolean castingShadow) {
		this.castingShadow = castingShadow;
	}

	public boolean doesRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}
}
