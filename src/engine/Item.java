package engine;

public class Item extends GraphicalObject{
	
	private String name, description;
	
	public Item(String n, String d) {
		this();
		this.name = n;
		this.description = d;
	}
	
	public Item() {
		this.name = "Unnamed Object";
		this.description = "Unidentified Object";
		
	}
	
	public Item(String n) {
		this();
		this.name = n;
	}

	@Override
	public int compareTo(WorldObject o) {
		
		super.compareTo(o);
		
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		
		if (o instanceof GraphicalObject) {

			GraphicalObject g = (GraphicalObject) o;
			
			if (this.getId() == g.getId()) {

			}

		}
		
		return false;
	}

	@Override
	public boolean equals(WorldObject o) {
		
		return this.equals(o);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
