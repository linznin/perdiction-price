package org.chening.text.dic;

import org.chening.text.semantic.SemanticConstants;

import java.io.*;
import java.util.*;

/**
 * Created by linznin on 2017/2/3.
 *
 */
public class CliwcDictionary implements SemanticConstants {

    private File dicFile;

    private ArrayList<String> classesCode = new ArrayList<>();

    private HashMap<String,ArrayList<String>> dicMach = new HashMap<>();

    private HashMap<String,String> targetSet = new HashMap<>();

    private ArrayList<String> title = new ArrayList<>();

    private ArrayList<String> keySet = new ArrayList<>();

    public CliwcDictionary(){}

    public CliwcDictionary(File dic) {
        this.dicFile = dic;
    }

    // <titleCode,words>
    public HashMap<String,ArrayList<String>> getDicClass () {
        ArrayList<String> classTitle = new ArrayList<>(Arrays.asList(targetFeature));
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
                        targetSet.put(title,ls[0]);
                        dicMach.put(ls[0],new ArrayList<String>());
                    }
                }
            }
            // 字典內容
            while ((line = br.readLine()) !=null) {
                String[] wordDic = line.split("\t");
                String word = wordDic[0];
                for (int i=1;i<wordDic.length;i++) {
                    if (classesCode.contains(wordDic[i])){
                        dicMach.get(wordDic[i]).add(word);
                    }
                }
            }


            System.out.println("classesCode = " + dicMach + "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator it = ((HashMap<String, String>) targetSet.clone()).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            title.add(pair.getKey().toString());
            keySet.add(pair.getValue().toString());
            it.remove();
        }

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

    public HashMap<String, String> getTargetSet() {
        return targetSet;
    }

    public void setTargetSet(HashMap<String, String> targetSet) {
        this.targetSet = targetSet;
    }

    public ArrayList<String> getKeySet() {
        return keySet;
    }

    public void setKeySet(ArrayList<String> keySet) {
        this.keySet = keySet;
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }
}
