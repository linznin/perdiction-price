package org.gandhim.pso.Problem;

import java.util.Arrays;

/**
 * Created by linznin on 2017/3/28.
 */
public class SemanticProblemSet extends ProblemSet {

    public SemanticProblemSet(){}

    public SemanticProblemSet(String org_path,int dimension){
        this.org_path = org_path;
        this.problem_dimension = dimension;
    }

    protected double executeSVM(int[] c){
        System.out.println("c = [" + Arrays.toString(c) + "]");
        double accuracy = 0;

        trainData = org_path;
        gamma = ""+Math.pow(2, c[0]);
        cost = ""+Math.pow(2, c[1]);

        accuracy = svmDriver.executeSVM(gamma, cost, trainData);

        System.out.println("gamma = [" + gamma + "] , cost = ["+cost+"]");
        System.out.println("SVM Classification is done! The accuracy is " + accuracy);
        System.out.println("trainData = [" + trainData + "]");

        return accuracy;
    }
}
