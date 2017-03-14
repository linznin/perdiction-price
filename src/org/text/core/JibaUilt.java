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
        CustomDictionary segDictionary = new CustomDictionary(new File(dicPath));
//        segDictionary.setDicFile(new File(dicFile));
        ArrayList<String> classTitle = new ArrayList<>(Arrays.asList(searchFile));
        dicMach = segDictionary.getDicClass();

        HashMap<String,ArrayList<String>> filesClasses = scanFolder( new File(newsPath),"txt");

        // csv title
        csvTitle(classTitle,new File(resultPath));
        // csv content
        csvContent(filesClasses,new ArrayList<>(dicMach.keySet()),new File(resultPath));
    }

    public void csvTitle(ArrayList<String> titles, File csvFile) {
        String splitMark = ", ";
        String csvTitle = "FileName".concat(splitMark);
        String title = titles.stream()
                .collect(Collectors.joining(splitMark));
        csvTitle = csvTitle.concat(title);
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
