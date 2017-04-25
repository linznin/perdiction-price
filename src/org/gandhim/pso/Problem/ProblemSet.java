package org.gandhim.pso.Problem;

import org.chening.text.core.Constants;
import org.gandhim.pso.Location;
import org.gandhim.pso.PSOConstants;
import org.gandhim.svm.SvmDriver;
import org.gandhim.svm.svm_train;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the problem to be solved
// to find an x and a y that minimize the function below:
// f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
// where 1 <= x <= 4, and -1 <= y <= 1

// you can modify the function depends on your needs
// if your problem space is greater than 2-dimensional space
// you need to introduce a new variable (other than x and y)

public class ProblemSet implements PSOConstants, Constants {
	public final int[] LOC_HIGH = {1,20}; //[gamma ,cost]
	public final int[] LOC_LOW = {-20,1};
	public final int VEL_LOW = -5;	//最小速度
	public final int VEL_HIGH = 5;	//最大速度
	
	public final double ERR_TOLERANCE = 1; // the smaller the tolerance, the more accurate the result,
	                                                  // but the number of iteration is increased   
													  //最小誤差

    public String gamma = GAMMA;
    public String cost = COST;
	public int problem_dimension = PROBLEM_DIMENSION+2;

	SvmDriver svmDriver = new SvmDriver();

    protected String org_path;
    protected String trainData;

    public ProblemSet(){}

    public ProblemSet(String org_path,int dimension){
    	this.org_path = org_path;
    	this.problem_dimension = dimension-2;
	}

	public double evaluate(Location location) {
		int[] local = location.getLoc(); // the location
		return executeSVM(local);
	}

	protected double executeSVM(int[] c){
		System.out.println("c = [" + Arrays.toString(c) + "]");
		double accuracy = 0;
		trainData = prepareData(c);

		accuracy = svmDriver.executeSVM(gamma, cost, trainData);

		System.out.println("gamma = [" + gamma + "] , cost = ["+cost+"]");
		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		System.out.println("trainData = [" + trainData + "]");

		removeTrainData();

		return accuracy;
	}
	
	protected String prepareData(int[] c){
		//給予features 最大特徵數
		boolean[] features = new boolean[problem_dimension];

		gamma = ""+Math.pow(2, c[0]);
		cost = ""+Math.pow(2, c[1]);

		//依照解碼後c 給予features 選取與否
		for (int i = 2 ;i< c.length;i++){
			if (c[i] == 0 ){
				features[i-2] = false;
			} else {
				features[i-2] = true;
			}
		}
		//回傳測試資料檔名稱
		return makeTrainData(features, org_path);
	}

	protected String makeTrainData(boolean[] features,String path){
		// 測試資料格式 <class> <lable>:<value> <lable>:<value>
		//trainData = org_path+".t";
		File org_data = new File(path);
		String trainData = TRAIN_PATH+org_data.getName()+".t";
		try (BufferedReader br = new BufferedReader(new FileReader(org_data)))
		{
			String rowData = "";
			PrintWriter writer = new PrintWriter(trainData, "UTF-8");
			//依行讀入資料
			String line;
			while ((line = br.readLine()) != null) {
				//依照空白分割 lable
				String[] row = line.split(" ");
				rowData = row[0];
				// 將不必要之feature 去除
				int index = 1;
				for (int i=1;i<row.length;i++){
					//將 lable 編號 與 解碼後feactures 對應 並依照feactures結果選入特徵
					String[] topicValue = row[i].split(":");
					if(features[Integer.parseInt(topicValue[0])-1]){
						rowData += " "+index+":"+topicValue[1];
						index++;
					}
				}
//				System.out.println(rowData);
				writer.println(rowData);
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return trainData;
	}

	private void removeTrainData() {
		new File(trainData).delete();
	}
}
