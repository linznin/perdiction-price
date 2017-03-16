package org.text.core;

import org.text.dic.CustomDictionary;
import org.text.dic.SegDictionary;
import org.text.jiba.JibaTools;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by linznin on 2017/2/17.
 */
public class JibaUilt extends FileUilt {

    public void execute() {
//        SegDictionary segDictionary = new SegDictionary(new File(dicFile));
        CustomDictionary segDictionary = new CustomDictionary(new File(dicPath));
        dicMach = segDictionary.getDicClass();

        HashMap<String,ArrayList<String>> filesClasses = scanFolder( new File(newsPath),"txt");

        // csv title
        csvTitle(segDictionary.getTitle(),new File(resultPath));
        // csv content
        csvContent(filesClasses,segDictionary.getKeySet(),new File(resultPath));
    }

    public void csvTitle(ArrayList<String> titles, File csvFile) {
        String splitMark = ",";
        String csvTitle = "FileName".concat(splitMark);

        for (String title : titles){
            csvTitle = csvTitle.concat(title).concat(splitMark);
        }

        csvTitle = csvTitle.substring(0,csvTitle.length()-1);
        writeLine(csvFile,csvTitle);
    }

    @Override
    public ArrayList<String> analysis(String text) {
        JibaTools jibaTools = new JibaTools();
        ArrayList<String> result;
        try {
            result = jibaTools.segText(text);
        } catch (Exception e) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void csvContent(HashMap<String, ArrayList<String>> filesClasses,ArrayList<String> titles, File csvFile) {
        String splitMark = ", ";
        for (String fileName : filesClasses.keySet()) {
            String content = fileName.replaceAll(",", "").replace(".txt","");
            ArrayList<String> uniqueClass = filesClasses.get(fileName);
            HashMap<String, Integer> frequencymap = new HashMap<>();
            for (String title : titles) {
                Integer featureCount = 0;
                for (String wordFeature : uniqueClass) {
                    if (title.equals(wordFeature)) {
                        featureCount++;
                    }
                }
                frequencymap.put(title,featureCount);
                content = content.concat(splitMark).concat(featureCount.toString());
            }
            writeLine(csvFile, content);
        }
    }

}
