package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// bean class to represent location

public class Location {
	// store the Location in an array to accommodate multi-dimensional problem space
	private boolean[] loc;//[x,y,......]

	public Location(boolean[] loc) {
		super();
		this.loc = loc;
	}

	public boolean[] getLoc() {
		return loc;
	}

	public void setLoc(boolean[] loc) {
		this.loc = loc;
	}
	
}
