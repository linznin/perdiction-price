package org.gandhim.csv;

import org.gandhim.pso.PSOConstants;
import org.text.core.FileUilt;

import java.io.*;
import java.util.ArrayList;

public class CsvFile implements PSOConstants {

    FileUilt fileUilt = new FileUilt();

    public String genRowData(String path,String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(path+fileName)))
        {
            String rowData = "";
            PrintWriter writer = new PrintWriter(path+fileName+"train", "UTF-8");
            //依行讀入資料
            String line = "";
            while ((line = br.readLine()) != null) {
                //依照空白分割 lable
                String[] row = line.split(",");
                rowData = row[0];
                for (int i = 1; i < row.length; i++) {
                    rowData += " "+i+":"+row[i];
                }
                System.out.println(rowData);
                writer.println(rowData);
                rowData = "";
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void genCSVResult(String fileName,String correct,String location){
        ArrayList<String> result = new ArrayList<>();
        result.add(fileName);
        result.add(String.valueOf(C1));
        result.add((String.valueOf(W)));
        result.add(correct);
        result.add(location);
        fileUilt.writeLine(new File(RESULT_CSV),result.toString());
    }


}