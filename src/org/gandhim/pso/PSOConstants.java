package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 10;
	int MAX_ITERATION = 10;
	int PROBLEM_DIMENSION = 13;
	double C1 = 2;
	double C2 = 2;
	double W_UPPERBOUND = 1.0;
	double W_LOWERBOUND = 0.8;
	String ORG_PATH = "/Users/linznin/tmp/data/";
}
