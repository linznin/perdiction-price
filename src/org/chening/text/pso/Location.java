package org.chening.text.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// bean class to represent location

public class Location {
	// store the Location in an array to accommodate multi-dimensional problem space
	private int[] loc;//[x,y,......]

	public Location(int[] loc) {
		super();
		this.loc = loc;
	}

	public int[] getLoc() {
		return loc;
	}

	public void setLoc(int[] loc) {
		this.loc = loc;
	}
	
}
