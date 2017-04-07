package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem

import org.chening.text.core.Constants;
import org.gandhim.csv.CsvFile;
import org.gandhim.pso.Problem.ProblemSet;
import org.gandhim.pso.Problem.SemanticLdaProblemSet;
import org.gandhim.pso.Problem.SemanticProblemSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

class PSOProcess implements PSOConstants, Constants {
	private Vector<Particle> swarm = new Vector<>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<>();
	private double gBest = 0;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
	private Random generator = new Random();

	private int locationSize = PROBLEM_DIMENSION+2;
	private String dataPath = LDA_DATA_PATH +ORG_DATA;

	double w = W;
	double c = C1;

	private SemanticLdaProblemSet problemSet = new SemanticLdaProblemSet();

	public PSOProcess(){}

	public PSOProcess(String path, String dimension){
		this.locationSize = Integer.parseInt(dimension)+2;
		this.dataPath = path;
		this.problemSet = new SemanticLdaProblemSet(dataPath, locationSize);
	}

	public PSOProcess(String path, String dimension, double cVaule, double wVaule){
		this.locationSize = Integer.parseInt(dimension)+2;
		this.dataPath = path;
		this.c = cVaule;
		this.w = wVaule;
		this.problemSet = new SemanticLdaProblemSet(dataPath, locationSize);
	}

	synchronized void execute() {
		long time1 = System.currentTimeMillis();

        //族群初始化
		initializeSwarm();
		updateFitnessList();
		for(int i=0; i<SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}

		updateBestFitness();
		int t = 0;
		int limit = 0;

		//終止條件   最大疊代數 及 最小誤差
//		while (t < MAX_ITERATION && gBest < problemSet.ERR_TOLERANCE ){

		while (t < MAX_ITERATION && gBest < problemSet.ERR_TOLERANCE && limit < LIMIT_ERR){
			//權重遞減	
//			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);

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
                                (r1 * c) * ((double) pBestLocation.get(i).getLoc()[j]) - (double) p.getLocation().getLoc()[j] +
                                (r2 * c) * ((double) gBestLocation.getLoc()[j] - (double) p.getLocation().getLoc()[j]);

					if (j<2){ //gamma & cost location
						//limit Velocity
	                    if (newVel[j] > problemSet.VEL_HIGH){
	                        newVel[j] = problemSet.VEL_HIGH;
	                    } else if (newVel[j] < problemSet.VEL_LOW){
	                        newVel[j] = problemSet.VEL_LOW;
	                    }

						newLoc[j] = (int)(p.getLocation().getLoc()[j] + newVel[j]);

						//limit Location
						if (newLoc[j] > problemSet.LOC_HIGH[j]){
							newLoc[j] = problemSet.LOC_HIGH[j];
						} else if (newLoc[j] < problemSet.LOC_LOW[j]){
							newLoc[j] = problemSet.LOC_LOW[j];
						}

					} else {
						//feature location get new location
						//如果更改機率值小於亂數值則更改位置
						newLoc[j] = p.getLocation().getLoc()[j];
						if (PSOUtility.sigmoid(newVel[j]) < randId){
							if (newLoc[j] == 1) {
								newLoc[j] = 0;
							} else {
								newLoc[j] = 1;
							}
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
                double fitnessValue = problemSet.evaluate(loc);
                p.setFitnessValue(fitnessValue);

                fitnessValueList[i] = fitnessValue;
            }

			//更新最佳值
			if ( updateBestFitness() ) {
				limit = 0;
			} else {
				limit++;
			}
			
			System.out.println("ITERATION " + t + ": ");
			System.out.println("     Best : " + Arrays.toString(gBestLocation.getLoc()));
			System.out.println("     Value: " + gBest);
			
			t++;
		}
		long time2 = System.currentTimeMillis();

		System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
		System.out.println("     Best : " + Arrays.toString(gBestLocation.getLoc()));
		System.out.println("	 Value : "+gBest);
		System.out.println("	 Execute Time：" + (time2 - time1)/1000 + " s");
		recordResult(""+gBest,Arrays.toString(gBestLocation.getLoc()),(time2 - time1)/1000);
	}

	private boolean updateBestFitness() {
		boolean isUpdate = false;
		// step 1 - update pBest
		for(int i=0; i<SWARM_SIZE; i++) {
			if(fitnessValueList[i] > pBest[i]) {
				pBest[i] = fitnessValueList[i];
				pBestLocation.set(i, swarm.get(i).getLocation());
			}
		}

		// step 2 - update gBest
		int bestParticleIndex = PSOUtility.getMaxPos(fitnessValueList);
		if(fitnessValueList[bestParticleIndex] > gBest) {
			gBest = fitnessValueList[bestParticleIndex];
			gBestLocation = swarm.get(bestParticleIndex).getLocation();
			isUpdate = true;
		}

		return isUpdate;
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
					if (vel[j] > problemSet.VEL_HIGH){
						vel[j] = problemSet.VEL_HIGH;
					} else if (vel[j] < problemSet.VEL_LOW){
						vel[j] = problemSet.VEL_LOW;
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
            double fitnessValue = problemSet.evaluate(p.getLocation());
            p.setFitnessValue(fitnessValue);
            fitnessValueList[i] = fitnessValue;
		}
	}


	private void recordResult(String gBest,String location, Long time){
		CsvFile csvFile = new CsvFile();
		String fileName = new File(dataPath).getName();
		csvFile.genCSVResult(fileName , gBest, location.replace(",",";"), String.valueOf(c), String.valueOf(w) );

		try (FileWriter writer = new FileWriter(RESULT_FILE, true))
		{
			writer.append("****************************************************** \n");
			writer.append("Data file : "+fileName+"\n");
			writer.append("C : "+String.valueOf(c)+"\n");
			writer.append("W : "+String.valueOf(w)+"\n");
			writer.append("gBest : "+gBest+"\n");
			writer.append("location : "+location+"\n");
			writer.append("time : "+time+" s\n");
			writer.append("******************************************************\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
