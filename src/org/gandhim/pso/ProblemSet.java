package org.gandhim.pso;

import org.gandhim.svm.svm_predict;
import org.gandhim.svm.svm_train;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the problem to be solved
// to find an x and a y that minimize the function below:
// f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
// where 1 <= x <= 4, and -1 <= y <= 1

// you can modify the function depends on your needs
// if your problem space is greater than 2-dimensional space
// you need to introduce a new variable (other than x and y)

public class ProblemSet implements PSOConstants{
	public static final int[] LOC_HIGH = {1,20}; //[gamma ,cost]
	public static final int[] LOC_LOW = {-20,1};
	public static final int VEL_LOW = -3;	//最小速度
	public static final int VEL_HIGH = 3;	//最大速度
	
	public static final double ERR_TOLERANCE = 1; // the smaller the tolerance, the more accurate the result,
	                                                  // but the number of iteration is increased   
													  //最小誤差

    public static String gamma = "0.001953125";
    public static String cost = "512.0";
    public static final String nr_fold = "10";

	//http://blog.csdn.net/yangliuy/article/details/8041343
	//for libsvm
	public static double executeSVM(int[] c){
		System.out.println("c = [" + Arrays.toString(c) + "]");
		double accuracy = 0;
		String trainData = prepareData(c);
		//svm_train:
		//    param: String[], parse result of command line parameter of svm-train
		//    return: String, the directory of modelFile
		//svm_predect:
		//    param: String[], parse result of command line parameter of svm-predict, including the modelfile
		//    return: Double, the accuracy of SVM classification
		try {
            //svmtrain [options] training_set_file [model_file]
			//directory of -g gamma, -c cost,/* -v nr_fold,*/ training file, model file
			String[] trainArgs = {"-g", gamma, "-c", cost, "-v", nr_fold, ORG_PATH+trainData};
//			String[] trainArgs = {"-g", gamma, "-c", cost, ORG_PATH+trainData, ORG_PATH+trainData+".model"};

			//使用交叉驗證會直接回傳正確率 而一般會回傳 model file name
//			String modelFile = svm_train.main(trainArgs);
			accuracy = Double.parseDouble(svm_train.main(trainArgs));

            //svmpredict test_file model_file output_file
			//directory of test file, model file, result file
//			String[] testArgs = {ORG_PATH+trainData, modelFile, ORG_PATH+trainData+"_reslut"};
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

		return accuracy;
	}

	public static double evaluate(Location location) {
		int[] local = location.getLoc(); // the location
		double result = executeSVM(local);
		return result;
	}

	private static char[] decode(int i){

		char[] c = Integer.toBinaryString(i).toCharArray();
		System.out.println("decode "+i+" to "+String.valueOf(c));
		return c;
	}

	private static int encode(char[] c){
		int i = Integer.parseInt(c.toString());
		System.out.println("encode "+c.toString()+" to "+i);
		return i;
	}
	
	private static String prepareData(int[] c){
		// TODO: 2016/11/21  
		//給予features 最大特徵數
		boolean[] features = new boolean[PROBLEM_DIMENSION+2];

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

	private static String makeTrainData(boolean[] features){
		// 測試資料格式 <class> <lable>:<value> <lable>:<value>
		UUID uuid = UUID.randomUUID();
		long time  = new Date().getTime();
		String trainData = ORG_DATA+uuid+"_"+time+".t";
		try (BufferedReader br = new BufferedReader(new FileReader(ORG_PATH+ORG_DATA)))
		{
			String rowData = "";
			PrintWriter writer = new PrintWriter(ORG_PATH+trainData, "UTF-8");
			//依行讀入資料
			String line = "";
			while ((line = br.readLine()) != null) {
				//依照空白分割 lable
				String[] row = line.split(" ");
				rowData = row[0];
				// 將不必要之feature 去除
				for (int i=1;i<row.length;i++){
					//取得特徵之lable
					String[] feature = row[i].split(":");
					//將 lable 編號 與 解碼後feactures 對應 並依照feactures結果選入特徵
					if(features[Integer.parseInt(feature[0])-1]){
						rowData += " "+row[i];
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
	private static boolean featureDecode(){
		return true;
	}
}
