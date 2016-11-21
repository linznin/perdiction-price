package org.gandhim.csv;

import org.gandhim.pso.PSOConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by linznin on 2016/11/18.
 */
public class CsvFile implements PSOConstants {

    public String genRowData(String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(ORG_PATH+fileName)))
        {
            String rowData = "";
            PrintWriter writer = new PrintWriter(ORG_PATH+fileName+"train", "UTF-8");
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
}