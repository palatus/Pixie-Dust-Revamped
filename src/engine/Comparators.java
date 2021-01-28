package engine;

import java.awt.Point;
import java.util.Comparator;

public class Comparators {

	public static class IDComparator implements Comparator<GraphicalObject> {

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
	
	public static class shadowCastComparator implements Comparator<GraphicalObject> { // sort the objects by shadow ability

		@Override
		public int compare(GraphicalObject o1, GraphicalObject o2) {
			
			if(o1.CastsShadows() && !o2.CastsShadows()) {
				return -1;
			}
			
			if(!o1.CastsShadows() && o2.CastsShadows()) {
				return 1;
			}
			
			return 0;
		}

	}
	
	public static class distanceComparator implements Comparator<GraphicalObject> {

		private Point other = null;
		
		@Override
		public int compare(GraphicalObject o1, GraphicalObject o2) {
			
			Point one = new Point(o1.getX(), o1.getY());
			Point two = new Point(o2.getX(), o2.getY());
			
			if(other == null) {
				return 0;
			}
			
			if(one.distance(other) > two.distance(other)) {
				return 1;
			}
			
			if(one.distance(other) < two.distance(other)) {
				return -1;
			}
			
			return 0;
		}

		public Point getOther() {
			return other;
		}

		public void setOther(Point other) {
			this.other = other;
		}

	}
	
}