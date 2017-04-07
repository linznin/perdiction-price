package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 20;
	int MAX_ITERATION = 1000;
    int PROBLEM_DIMENSION = 30;
    int LIMIT_ERR = 10;
	double C1 = 2.5;
	double C2 = 2.5;
	double W_UPPERBOUND = 0.9;
	double W_LOWERBOUND = 0.4;
	double W = 0.9;


	String GAMMA = "0.001953125";
	String COST = "512.0";

}
