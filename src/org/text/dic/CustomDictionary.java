package org.text.dic;

import org.text.core.FileConstants;

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
public class CustomDictionary implements FileConstants{

    HashMap<String ,ArrayList<String>> dicMach = new HashMap<>();
    private File folder;


    public CustomDictionary(File floder){
        this.folder = floder;
    }

    public CustomDictionary() {}

    public static void main(String[] args) {
//        CustomDictionary customDictionary = new CustomDictionary("/Users/linznin/tmp/dic/dictionary");
//        customDictionary.getDicClass();
    }

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
                    br.readLine();
                    while ((line = br.readLine()) !=null && !"".equals(line)) {
                        classes.add(line);
                    }
                    dicMach.put(fileName,classes);
                }
            }
        }catch (Exception e) {

        }
        return dicMach;
    }

}
