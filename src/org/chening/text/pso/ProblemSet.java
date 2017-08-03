package org.chening.text.pso;

import org.chening.text.core.Constants;
import org.chening.text.core.ProSetting;
import org.chening.text.pso.Problem.LdaProbleSet;
import org.chening.text.pso.Problem.LdaSemanticProblemSet;
import org.chening.text.pso.Problem.SemanticProblemSet;
import org.chening.text.svm.SvmDriver;

import java.io.File;
import java.util.Arrays;

/**
 * 定義如何最佳化問題
 * 基本方法 只有LDA進行最佳化
 */

public class ProblemSet implements PSOConstants {
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

		removeTrainData();

		return accuracy;
	}

    /**
     * prepareData 將最佳化可能解轉換 gamma & cost 以及lda特徵轉換為true & false
     * @param c 每個特徵值
     * @return
     */
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
	    switch (ProSetting.OPTIMIZATION_FUNCTION) {
            case Constants.OPTIMIZATION_LDA:
                trainData = new LdaSemanticProblemSet().makeTrainData(features, path, ProSetting.TRAIN_PATH);
                break;
            case Constants.OPTIMIZATION_SEMANTIC:
                trainData = new SemanticProblemSet().makeTrainData(features, path, ProSetting.TRAIN_PATH);
                break;
            default:
                trainData = new LdaProbleSet().makeTrainData(features, path, ProSetting.TRAIN_PATH);
                break;
        }

		return trainData;
	}

	private void removeTrainData() {
		new File(trainData).delete();
	}
}
