package org.chening.text.semantic;

import org.chening.text.fileUilt.FileUilt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by linznin on 2017/6/18.
 */
public class SemanticFileReader {

    public void execute(){
        scanFolder(new File("/Users/linznin/tmp/semantic/data"));
    }

    public void scanFolder(File Path) {
        for (final File fileEntry : Path.listFiles()) {
            if (fileEntry.isDirectory()) {
                scanFolder(fileEntry);
            } else {
                //TODO
                readSemanticFile(fileEntry);
            }
        }
    }

    /*
        make semantic file to thesis format
        posemo	negemo	affect	sad	cogmech	anx	anger
     */
    private void readSemanticFile(File org_file){
        try
        {
            FileUilt fileUilt = new FileUilt();
            File resultCSV = new File("/Users/linznin/tmp/result_csv/"+org_file.getName()+".csv");
            BufferedReader br = new BufferedReader(new FileReader(org_file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String stackMark = row[0];
                int featureSize = row.length-8;
                ArrayList<String> semanticCount = new ArrayList<>();
                for (int i=1;i<8;i++){
                    semanticCount.add(row[featureSize+i]);
                }
                fileUilt.writeLine(resultCSV,stackMark+","+String.join(",",semanticCount));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
