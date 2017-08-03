package org.chening.text.dic;

import org.chening.text.semantic.SemanticConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by linznin on 2017/2/24.
 * 為了符合自定義字典
 * 並且預期字典為多個檔案存放入同一字典目錄
 */
public class CustomDictionary implements SemanticConstants {

    HashMap<String ,ArrayList<String>> dicMach = new HashMap<>();

    private File folder;

    private HashMap<String,String> targetSet = new HashMap<>();

    private ArrayList<String> title = new ArrayList<>();

    private ArrayList<String> keySet = new ArrayList<>();

    public CustomDictionary(File floder){
        this.folder = floder;
    }

    public CustomDictionary() {}

    public HashMap<String,ArrayList<String>> getDicClass() {
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile() && Arrays.asList(searchFile).contains(fileEntry.getName())) {
                    ArrayList<String> classes = new ArrayList<>();
                    String fileName = fileEntry.getName();
                    // 取得副檔名
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")+1);

                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line ;
                    String key = br.readLine();
                    while ((line = br.readLine()) !=null && !"".equals(line)) {
                        classes.add(line);
                    }
                    dicMach.put(fileName,classes);
                    title.add(fileName);
                    keySet.add(fileName);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dicMach;
    }

    public HashMap<String, String> getTargetSet() {
        return targetSet;
    }

    public void setTargetSet(HashMap<String, String> targetSet) {
        this.targetSet = targetSet;
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }

    public ArrayList<String> getKeySet() {
        return keySet;
    }

    public void setKeySet(ArrayList<String> keySet) {
        this.keySet = keySet;
    }
}
