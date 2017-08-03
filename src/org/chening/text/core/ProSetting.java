package org.chening.text.core;

/**
 * Created by linznin on 2017/3/27.
 */
public class ProSetting {
    public static String OPTIMIZATION_FUNCTION = "0";
    public static boolean KEEP_JIEBA_RESULT = true;

    public static String ORG_PATH ="/Users/linznin/tmp";

    public static String NEWS_PATH;
    public static String LDA_DATA_PATH;
    public static String TRAIN_PATH;
    public static String RESULT_PATH ;
    public static String JIEBA_RESULT_PATH;
    public static String CLIWC_DICFILE;
    public static String SEMANTIC_PATH;

    public static String SEMANTIC_RESULT_FILE;
    public static String PSO_RESULT_FILE;
    public static String PSO_RESULT_CSV;

    public static String ORG_DATA = "/30/computer_30_1d";

    public static void genPath(){
        NEWS_PATH = ORG_PATH+"/news";
        LDA_DATA_PATH = ORG_PATH+"/data";
        TRAIN_PATH = ORG_PATH+"/train";
        RESULT_PATH = ORG_PATH+"/result";
        JIEBA_RESULT_PATH = RESULT_PATH+"/jieba";
        CLIWC_DICFILE = ORG_PATH + "/dic/cliwc.dic";
        SEMANTIC_PATH = RESULT_PATH + "/semantic";
        SEMANTIC_RESULT_FILE = SEMANTIC_PATH + "/semantic.csv";
        PSO_RESULT_FILE = RESULT_PATH+"/pso_result";
        PSO_RESULT_CSV = RESULT_PATH+"/pso_result.csv";
    }
}
