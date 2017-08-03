package org.chening.text.fileUilt;

import org.chening.text.semantic.SemanticUilt;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by linznin on 2017/6/19.
 */
public class LdaFileReader {

    public void execute(){
        mkTextPatant();
//        scanFolder(new File("/Users/linznin/tmp/result/thasis"));
    }

    public void scanFolder(File Path) {
        for (final File fileEntry : Path.listFiles()) {
            if (fileEntry.isDirectory()) {
                scanFolder(fileEntry);
            } else {
                //TODO
                readLdaFile(fileEntry);
            }
        }
    }

    /*
        make lda pso file to thesis format

     */
    private void readLdaFile(File org_file){
        try
        {
            SemanticUilt semanticUilt = new SemanticUilt();
            File resultCSV = new File("/Users/linznin/tmp/result_csv/"+org_file.getName());
            BufferedReader br = new BufferedReader(new FileReader(org_file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String fileName = row[0];
                String accury = row[3];
                String[] features = row[4].split(";");
                ArrayList<String> selectFeatrues = new ArrayList<>();
                String gamma = features[0];
                String cost = features[1];
                for(int i=2;i<features.length;i++){
                    String feature = features[i].replaceAll("\\s+","");
                    if (feature.equals("1")){
                        String index = String.valueOf(i-2);
                        selectFeatrues.add("T"+index);
                    }
                }
                semanticUilt.writeLine(resultCSV,fileName+","+accury+","+gamma+","+cost+","+String.join(";",selectFeatrues));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mkTrainData(){
        File file = new File("/Users/linznin/tmp/LDA/data/train/食品類.dat");
        File trainFile = new File("/Users/linznin/tmp/LDA/data/train/food_train.dat");
        ArrayList<String> textLine = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int i=0;
            while ((line = br.readLine()) != null) {
                String row = line.split(",")[0];
                String text = row.replaceAll("\"","");
                textLine.add(i,text);
                i++;
            }
            SemanticUilt semanticUilt = new SemanticUilt();

            semanticUilt.writeLine(trainFile,i+"");
            for (String text: textLine) {
                semanticUilt.writeLine(trainFile,text);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mkTextPatant() {
        File file = new File("/Users/linznin/tmp/resultPreview");
        File trainFile= new File("/Users/linznin/tmp/textcombine.fin");
        SemanticUilt semanticUilt = new SemanticUilt();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] word= line.split(" ");
                String tmp ="";
                for (int i=0;i<word.length;i++){
                    if (word[i].contains(":")){
                        String a = word[i].split(":")[1];
                        tmp += a+" ";
                    } else {
                        tmp += word[i]+" ";
                    }

                    if ((i+1)%10==0 || i+1==word.length){
                        semanticUilt.writeLine(trainFile,tmp);
                        tmp="";
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
