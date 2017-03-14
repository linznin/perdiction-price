package org.gandhim.pso;

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

public class ProblemSet implements PSOConstants{
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
    public final String nr_fold = "10";

    private String org_path;
    private String trainData;

    public ProblemSet(){}

    public ProblemSet(String org_path,int dimension){
    	this.org_path = org_path;
    	this.problem_dimension = dimension;
		this.trainData = org_path+".t";
	}

	//http://blog.csdn.net/yangliuy/article/details/8041343
	//for libsvm
	private double executeSVM(int[] c){
		System.out.println("c = [" + Arrays.toString(c) + "]");
		double accuracy = 0;
		prepareData(c);
		//svm_train:
		//    param: String[], parse result of command line parameter of svm-train
		//    return: String, the directory of modelFile
		//svm_predect:
		//    param: String[], parse result of command line parameter of svm-predict, including the modelfile
		//    return: Double, the accuracy of SVM classification
		try {
            //svmtrain [options] training_set_file [model_file]
			//directory of -g gamma, -c cost,/* -v nr_fold,*/ training file, model file
			String[] trainArgs = {"-g", gamma, "-c", cost, "-v", nr_fold, trainData};
//			String[] trainArgs = {"-g", gamma, "-c", cost, DATA_PATH+trainData, DATA_PATH+trainData+".model"};

			//使用交叉驗證會直接回傳正確率 而一般會回傳 model file name
//			String modelFile = svm_train.main(trainArgs);
			accuracy = Double.parseDouble(svm_train.main(trainArgs));

            //svmpredict test_file model_file output_file
			//directory of getDicClass file, model file, result file
//			String[] testArgs = {DATA_PATH+trainData, modelFile, DATA_PATH+trainData+"_reslut"};
//			accuracy = svm_predict.main(testArgs);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("gamma = [" + gamma + "] , cost = ["+cost+"]");
		System.out.println("SVM Classification is done! The accuracy is " + accuracy);
		System.out.println("trainData = [" + trainData + "]");

		//Test for cross validation
		//String[] crossValidationTrainArgs = {"-v", "10", "UCI-breast-cancer-tra"};// 10 fold cross validation
		//modelFile = svm_train.main(crossValidationTrainArgs);
		//System.out.print("Cross validation is done! The modelFile is " + modelFile);

		removeTrainData();

		return accuracy;
	}

	private void removeTrainData() {
		new File(trainData).delete();
	}

	public double evaluate(Location location) {
		int[] local = location.getLoc(); // the location
		return executeSVM(local);
	}

	private char[] decode(int i){

		char[] c = Integer.toBinaryString(i).toCharArray();
		System.out.println("decode "+i+" to "+String.valueOf(c));
		return c;
	}

	private int encode(char[] c){
		//TODO
		int i = Integer.parseInt(c.toString());
		System.out.println("encode "+c.toString()+" to "+i);
		return i;
	}
	
	private String prepareData(int[] c){
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
		return makeTrainData(features);
	}

	private String makeTrainData(boolean[] features){
		// 測試資料格式 <class> <lable>:<value> <lable>:<value>
//		trainData = org_path+".t";
		try (BufferedReader br = new BufferedReader(new FileReader(org_path)))
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
				int count = 1;
				for (int i=1;i<row.length;i++){
					//取得特徵之lable
					String[] feature = row[i].split(":");
					//將 lable 編號 與 解碼後feactures 對應 並依照feactures結果選入特徵
					if(features[Integer.parseInt(feature[0])-1]){
						rowData += " "+count+":"+feature[1];
						count++;
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
}
