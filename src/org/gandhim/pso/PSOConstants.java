package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 20;
	int MAX_ITERATION = 100;
    int PROBLEM_DIMENSION = 30;
    int LIMIT_ERR = 15;
	double C1 = 2.5;
	double C2 = 2.5;
	double W_UPPERBOUND = 0.9;
	double W_LOWERBOUND = 0.4;
	double W = 0.8;
	String ORG_PATH = "/Users/linznin/tmp/data/";
//	String ORG_PATH = "C:\\tmp\\data\\";
	String ORG_DATA = "food_30_1d";
	String RESULT_FILE = "result";
}
