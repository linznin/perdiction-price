package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 20;
	int MAX_ITERATION = 2000;
    int PROBLEM_DIMENSION = 30;
    int LIMIT_ERR = 20;
	double C1 = 1;
	double C2 = 1;
	double W_UPPERBOUND = 0.9;
	double W_LOWERBOUND = 0.4;
	double W = 0.8;


	String GAMMA = "0.001953125";
	String COST = "512.0";

	String ORG_PATH ="/Users/linznin/tmp/";
	//	String ORG_PATH = "C:\\tmp\\";
	String DATA_PATH = ORG_PATH+"data/";
	String ORG_DATA = "30/food_30_1d";

	String RESULT_PATH = ORG_PATH+"result/";
	String RESULT_FILE = RESULT_PATH+"result.txt";
	String RESULT_CSV = RESULT_PATH+"result.csv";
}
