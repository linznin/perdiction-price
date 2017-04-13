package org.gandhim.svm;

import org.chening.text.core.Constants;
import org.text.core.TextConstants;

import java.io.File;
import java.io.IOException;

public class SvmDriver implements Constants, TextConstants{

    public final String nr_fold = "10";

    //http://blog.csdn.net/yangliuy/article/details/8041343
    //for libsvm
    public double executeSVM(String gamma, String cost, String trainData){
        double accuracy = 0;
        //svm_train:
        //    param: String[], parse result of command line parameter of svm-train
        //    return: String, the directory of modelFile
        //svm_predect:
        //    param: String[], parse result of command line parameter of svm-predict, including the modelfile
        //    return: Double, the accuracy of SVM classification
        try {
            //svmtrain [options] training_set_file [model_file]
            //directory of -g gamma, -c cost,/* -v nr_fold,*/ training file, model file
            String scaleFile = scaleData(trainData);

            String[] trainArgs = {"-g", gamma, "-c", cost, "-v", nr_fold, scaleFile};
//			String[] trainArgs = {"-g", gamma, "-c", cost, LDA_DATA_PATH+trainData, LDA_DATA_PATH+trainData+".model"};

            //使用交叉驗證會直接回傳正確率 而一般會回傳 model file name
//			String modelFile = svm_train.main(trainArgs);
            accuracy = Double.parseDouble(svm_train.main(trainArgs));

            new File(scaleFile).delete();
            //svmpredict test_file model_file output_file
            //directory of getDicClass file, model file, result file
//			String[] testArgs = {LDA_DATA_PATH+trainData, modelFile, LDA_DATA_PATH+trainData+"_reslut"};
//			accuracy = svm_predict.main(testArgs);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return accuracy;
    }

    public String scaleData(String trainFile){
        String scaleFile = trainFile+".scale";
        String[] scaleArgs = {"-d",scaleFile,trainFile};
        try {
            svm_scale.main(scaleArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaleFile;
    }


}
