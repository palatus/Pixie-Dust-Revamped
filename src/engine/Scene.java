package engine;

import java.awt.Point;

import light.LightMap;

public class Scene {

	private Backpack sceneObjects = new Backpack(); 
	private LightMap lightmap;
	private Window owner;
	private Point objectSortReference;
	
	public Scene() { // Keeps track of a full scene (one display on the stage) through its GraphicalObject data
		
	}
	
	public void sortObjectsByShadow() {
		this.getSceneObjects().getContents().sort(new Comparators.shadowCastComparator());
	}
	
	public void pushSceneObject(GraphicalObject g) { // essential for maintaining lightmap accuracy
		this.getSceneObjects().addItem(g);
		this.sortObjectsByShadow();
	}

	public Scene(Window w) {
		owner = w;
	}
	
	public Backpack getSceneObjects() {
		return sceneObjects;
	}

	public void setSceneObjects(Backpack sceneObjects) {
		this.sceneObjects = sceneObjects;
	}

	public LightMap getLightmap() {
		return lightmap;
	}

	public void setLightmap(LightMap lightmap) {
		this.lightmap = lightmap;
		this.getLightmap().setOwner(owner);
	}

	public Window getOwner() {
		return owner;
	}

	public void setOwner(Window owner) {
		this.owner = owner;
	}

	public Point getObjectSortReference() {
		return objectSortReference;
	}

	public void setObjectSortReference(Point objectSortReference) {
		this.objectSortReference = objectSortReference;
	}
	
}
