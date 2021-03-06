package org.chening.text.pso.Problem;

import java.io.*;

/**
 * Created by linznin on 2017/3/28.
 * 單純使用情緒資料 最佳化C與W
 * 來源資料格式： LDA feature,...,semantic feature
 * 挑選情緒資料進入訓練資料
 */
public class SemanticProblemSet {

    public String makeTrainData(boolean[] features,String path, String trainPath){
        // 測試資料格式 <class> <lable>:<value> <lable>:<value>
        //trainData = org_path+".t";
        File org_data = new File(path);
        String trainData = trainPath+org_data.getName()+".train";
        try (BufferedReader br = new BufferedReader(new FileReader(org_data)))
        {
            int featureSize = 7;
            String rowData = "";
            PrintWriter writer = new PrintWriter(trainData, "UTF-8");
            //依行讀入資料
            String line;
            while ((line = br.readLine()) != null) {
                //依照空白分割 lable
                String[] row = line.split(",");
                rowData = row[0];

                int index = 1;
                for (int i=0;i<featureSize;i++){
                    rowData += " "+index+":"+row[row.length - featureSize + i];
                    index++;
                }
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
