package engine;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class IDList extends ArrayList<GraphicalObject>{

	private boolean reserved = false;
	
	public IDList() {
		setReserved(false);
	}
	
	public IDList(boolean f) {
		setReserved(f);
	}
	
	public void sort() {
		Collections.sort(this,new Comparators.IDComparator());
	}
	
	public void clean() { // remove used id item entries that have no owners (outside of reserved ones)
		
		for(int i = 0; i<this.size(); i++) {
			if(this.get(i).getOwnerGroup() == null) {
					this.remove(i);
					i--;
			}
		}
		
	}

	public boolean contains(Object o) {

		if (o instanceof GraphicalObject) {

			GraphicalObject gg = (GraphicalObject) o;

			for (GraphicalObject g : this) {
				if (gg.equals(g)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
}
