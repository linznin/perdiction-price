package org.chening.text.pso.Problem;

import java.io.*;

/**
 * Created by linznin on 2017/8/1.
 */
public class LdaProbleSet {
    /**
     * makeTrainData
     * @param features 每個LDA特徵是否取用
     * @param path 來源資料位置
     * @return
     */
    public String makeTrainData(boolean[] features,String path, String trainPath){
        // 測試資料格式 <class> <lable>:<value> <lable>:<value>
        //trainData = org_path+".t";
        File org_data = new File(path);
        String trainData = trainPath+org_data.getName()+".t";
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
