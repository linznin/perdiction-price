package org.text.dic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linznin on 2017/2/3.
 *
 */
public class SegDictionary {



    private File dicFile;

    private ArrayList<String> classesCode = new ArrayList<>();

    private HashMap<String,ArrayList<String>> dicMach = new HashMap<>();

    // <titleCode,words>
    public HashMap<String,ArrayList<String>> getDicClass (ArrayList<String> classTitle) {
        ArrayList<String> classes = new ArrayList<>();

        try {
            // 讀取字典表頭
            BufferedReader br = new BufferedReader(new FileReader(dicFile));
            String line;
            int isTitleEnd = 0;
            while ( isTitleEnd != 2 && ((line = br.readLine()) !=null) ) {
                if (line.equals("%")) {
                    isTitleEnd ++;
                } else {
                    String[] ls = line.split("\t");
                    String title = ls[1];
                    if (classTitle.contains(title)) {
                        classesCode.add(ls[0]);
                        dicMach.put(ls[0],new ArrayList<String>());
                    }
                }
            }
            while ((line = br.readLine()) !=null) {
                String[] wordDic = line.split("\t");
                String word = wordDic[0];
                for (int i=1;i<wordDic.length;i++) {
                    if (classesCode.contains(wordDic[i])){
                        ArrayList<String> wordClass = dicMach.get(wordDic[i]);
                        wordClass.add(word);
                    }
                }
            }


            System.out.println("classesCode = " + dicMach + "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setClassesCode(classes);
        return  dicMach;
    }


    public File getDicFile() {
        return dicFile;
    }

    public void setDicFile(File dicFile) {
        this.dicFile = dicFile;
    }

    public ArrayList<String> getClassesCode() {
        return classesCode;
    }

    public void setClassesCode(ArrayList<String> classesCode) {
        this.classesCode = classesCode;
    }

    public HashMap<String, ArrayList<String>> getDicMach() {
        return dicMach;
    }

    public void setDicMach(HashMap<String, ArrayList<String>> dicMach) {
        this.dicMach = dicMach;
    }
}
