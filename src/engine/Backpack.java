package engine;

import java.util.ArrayList;
import java.util.Collections;

public class Backpack{

	private int maxSlots = 2048; // max scene size
	private Scene owner;
	
	private ArrayList<GraphicalObject> contents = new ArrayList<GraphicalObject>();
	
	public Backpack(int size) {
		this.maxSlots = size;
	}
	
	public Backpack() {
	}
	
	public boolean full() {
		return this.contents.size() >= this.maxSlots;
	}
	
	public void sort() {
		Collections.sort(this.contents);
	}
	
	public void ReportList() {
		
		for(int i = 0; i<this.contents.size(); i++) {
			System.out.println("Slot "+i+": "+this.contents.get(i).getId());
		}
		
	}
	
	public boolean addItem(GraphicalObject i){
		
		if(this.contents.size() < this.maxSlots) {
			this.contents.add(i);
			i.setOwnerGroup(this);
			return true;
		} else {
			//System.err.println("No more space");
		}
		
		return false;
	}
	
	public void growBackpack(int s) {
		this.maxSlots += s;
	}

	public int getMaxSlots() {
		return maxSlots;
	}

	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}

	public ArrayList<GraphicalObject> getContents() {
		return contents;
	}

	public void setContents(ArrayList<GraphicalObject> contents) {
		this.contents = contents;
	}

	public Scene getOwner() {
		return owner;
	}

	public void setOwner(Scene owner) {
		this.owner = owner;
	}

}
