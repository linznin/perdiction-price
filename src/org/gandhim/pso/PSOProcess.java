package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

class PSOProcess implements PSOConstants {
	private Vector<Particle> swarm = new Vector<>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
	private int locationSize = PROBLEM_DIMENSION+2;
	
	private Random generator = new Random();

	private double accuracy = 0;
	
	void execute() {
        //族群初始化
		initializeSwarm();
		updateFitnessList();
		
		for(int i=0; i<SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int t = 0;
		double w;
		//double err = 9999;

		//終止條件   最大疊代數 及 最小誤差
		//while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
		while (t < MAX_ITERATION && accuracy < ProblemSet.ERR_TOLERANCE){
			//更新最佳值

            // step 1 - update pBest
			for(int i=0; i<SWARM_SIZE; i++) {
				if(fitnessValueList[i] > pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMaxPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] > gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}

			//權重遞減	
//			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			w = W;

			for(int i=0; i<SWARM_SIZE; i++) {
				System.out.println("\n SWARM "+i);
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
                double randId = generator.nextDouble();

				Particle p = swarm.get(i);
				
                //update velocity & location
				double[] newVel = new double[locationSize];
                int[] newLoc = new int[locationSize];
				for (int j = 0; j < locationSize; j++) {
                    //get new velocity
                    newVel[j] = (w * p.getVelocity().getPos()[j]) +
                                (r1 * C1) * (pBestLocation.get(i).getLoc()[j]) - p.getLocation().getLoc()[j] +
                                (r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]);

					if (j<2){ //gamma & cost location
						//limit Velocity
	                    if (newVel[j] > ProblemSet.VEL_HIGH){
	                        newVel[j] = ProblemSet.VEL_HIGH;
	                    } else if (newVel[j] < ProblemSet.VEL_LOW){
	                        newVel[j] = ProblemSet.VEL_LOW;
	                    }

						newLoc[j] = (int)(p.getLocation().getLoc()[j] + newVel[j]);

						//limit Location
						if (newLoc[j] > ProblemSet.LOC_HIGH[j]){
							newLoc[j] = ProblemSet.LOC_HIGH[j];
						} else if (newLoc[j] < ProblemSet.LOC_LOW[j]){
							newLoc[j] = ProblemSet.LOC_LOW[j];
						}

					} else { // feature location
						//get new location
						if (randId < PSOUtility.sigmoid(newVel[j])){
							newLoc[j] = 1;
						} else {
							newLoc[j] = 0;
						}
					}
                }

                // step 3 - update velocity
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				// step 4 - update location
				Location loc = new Location(newLoc);
				p.setLocation(loc);

                // step 5 - Fitness Function
                double fitnessValue = ProblemSet.evaluate(loc);
                p.setFitnessValue(fitnessValue);

                fitnessValueList[i] = fitnessValue;
            }

			accuracy = ProblemSet.evaluate(gBestLocation);


			System.out.println("ITERATION " + t + ": ");
			System.out.println("     Best : " + Arrays.toString(gBestLocation.getLoc()));
			System.out.println("     Value: " + accuracy);
			
			t++;
		}

		System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
		System.out.println("     Best : " + Arrays.toString(gBestLocation.getLoc()));
	}
	
	private void initializeSwarm() {
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) {
			p = new Particle();
			int[] loc = new int[locationSize];
            double[] vel = new double[locationSize];

            for (int j = 0; j < locationSize; j++) {
				// randomize velocity in the range defined in Problem Set
				vel[j] = Math.random();
				if ( j<2 ) { //gamma & cost location
					double random = generator.nextDouble();

					//limit Velocity
					if (vel[j] > ProblemSet.VEL_HIGH){
						vel[j] = ProblemSet.VEL_HIGH;
					} else if (vel[j] < ProblemSet.VEL_LOW){
						vel[j] = ProblemSet.VEL_LOW;
					}

					loc[j] = (int)(random*9+1);
					if (j == 0) {
						loc[j] = (int)-(random*19+1);
					} else {
						loc[j] = (int)(random*19+1);
					}
				} else { //feature location
					// randomize location inside a space defined in Problem Set
					if ( Math.random() < 0.5 ){
						loc[j] = 0;
					} else {
						loc[j] = 1;
					}

				}

            }
			Location location = new Location(loc);
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
	
	private void updateFitnessList() {
		for(int i=0; i<SWARM_SIZE; i++) {
            Particle p = swarm.get(i);
            double fitnessValue = ProblemSet.evaluate(p.getLocation());
            p.setFitnessValue(fitnessValue);
            fitnessValueList[i] = fitnessValue;
		}
	}
}
