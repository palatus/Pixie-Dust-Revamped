package engine;

public interface WorldObject extends Comparable<WorldObject>{ // Base game object interface
	
	public static IDList ListedID = new IDList(); // List of all IDs that are in use in RAM (Via Items)
	public static IDList ReservedID = new IDList(); // List of all reserved IDs for special Instantiations & data pointers (Via Items)
	
	public void shift(int hor, int vert); // Shift object's coordinates
	
	public int compareTo(WorldObject o); // compareTo with same child object
	
	public boolean equals(WorldObject o); // compareTo with same child object
	
	public void stretch(int width, int height); // stretch the dimensions
	
	public default void initReserved() { // load the reserved id list onto the used id list
		for (int i = 0; i < ReservedID.size(); i++) {
			ListedID.add(ReservedID.get(i));
		}
	}

	public default boolean checkID(int d) { // Does the used id list contain integer d

		GraphicalObject g = new GraphicalObject(d);
		return ListedID.contains(g);

	}

	public default boolean checkReservedList(int d) {  // Does the reserved id list contain integer d

		return ReservedID.contains(new GraphicalObject(d));

	}
	
	public static void ReportIDList() {
		
		for(GraphicalObject g: ListedID) {
			
			if(!(g instanceof Item)) {
				System.out.println("Object: "+g.getId());
			} else {
				System.out.println(((Item)g).getName()+": "+g.getId());
			}
			
		}
		
	}
	
}
