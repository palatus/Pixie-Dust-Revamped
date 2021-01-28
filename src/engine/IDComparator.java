package engine;

import java.util.Comparator;

public class IDComparator implements Comparator<GraphicalObject>{

	@Override
	public int compare(GraphicalObject o1, GraphicalObject o2) {
		
		if(o1.getId() < o2.getId()) {
			return -1;
		}
		
		if(o1.getId() > o2.getId()) {
			return 1;
		}
		
		return 0;
	}

}
