package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// just a simple utility class to find a minimum position on a list

public class PSOUtility {
	public static int getMinPos(double[] list) {
		int pos = 0;
		double minValue = list[0];
		
		for(int i=0; i<list.length; i++) {
			if(list[i] < minValue) {
				pos = i;
				minValue = list[i];
			}
		}
		
		return pos;
	}

	public static int getMaxPos(double[] list) {
		int pos = 0;
		double maxValue = list[0];

		for(int i=0; i<list.length; i++) {
			if(list[i] > maxValue) {
				pos = i;
				maxValue = list[i];
			}
		}

		return pos;
	}
	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}
