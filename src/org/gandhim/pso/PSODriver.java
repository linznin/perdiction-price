package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is a driver class to execute the PSO process

import org.gandhim.csv.CsvFile;

public class PSODriver {
	public static void main(String args[]) {
		//new CsvFile().genRowData("food_30_1d");
		new PSOProcess().execute();
	}
}
