package org.text.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by linznin on 2017/2/16.
 */
public class FileUilt implements FileConstants{

    protected HashMap<String,ArrayList<String>> dicMach;

    public ArrayList<String> analysis(String text) {
        ArrayList<String> result;
        try {
            result = new ArrayList<>(Arrays.asList(text.split("\\s+")));
        } catch (Exception e) {
            result = new ArrayList<>();
        }
        return result;
    }

    public ArrayList<String> analysisFile(String text) {
        ArrayList<String> featureList = analysis(text);
        ArrayList<String> fileClasses = new ArrayList<>();
        ArrayList<String> fileWords = new ArrayList<>();
        for (String feature: featureList){
            for (String key: dicMach.keySet()) {
                if (dicMach.get(key).contains(feature)){
                    fileClasses.add(key);
                    fileWords.add(feature);
                }
            }
        }

        return fileClasses;
    }

    public ArrayList<String> readFile(File file) {
        ArrayList<String> fileClasses = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String fileLine = "";
            while ((line = br.readLine()) !=null) {
                fileLine = fileLine.concat(line);
            }
            if (!"".equals(fileLine)) {
                fileClasses.addAll(analysisFile(fileLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileClasses;
    }

    public HashMap<String,ArrayList<String>> scanFolder(File Path, String fileType) {
        HashMap<String,ArrayList<String>> filesClasses = new HashMap<>();
        for (final File fileEntry : Path.listFiles()) {
            if (fileEntry.isDirectory()) {
                filesClasses.putAll(scanFolder(fileEntry, fileType));
            } else {
                // 取得副檔名
                int startIndex = fileEntry.getName().lastIndexOf(46) + 1;
                int endIndex = fileEntry.getName().length();
                if (fileEntry.getName().substring(startIndex, endIndex).equals(fileType)) {
                    filesClasses.put(fileEntry.getName(),readFile(fileEntry));
                }
            }
        }
        return filesClasses;
    }

    public void writeLine(File file, String line) {
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.write(line+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
