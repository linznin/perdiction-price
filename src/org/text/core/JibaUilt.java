package org.text.core;

import org.text.dic.SegDictionary;
import org.text.jiba.JibaTools;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by linznin on 2017/2/17.
 */
public class JibaUilt extends FileUilt {

    public HashMap<String,ArrayList<String>> dicMach;

    public void execute() {
        SegDictionary segDictionary = new SegDictionary();
        segDictionary.setDicFile(new File(dicPath));
        ArrayList<String> classTitle = new ArrayList<>(Arrays.asList(targetFeatrue));
        dicMach = segDictionary.getDicClass(classTitle);

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
            String csvContent = fileName.replaceAll(",", "").concat(splitMark);
            Set<String> uniqueClass = new HashSet<>(filesClasses.get(fileName));
            String content = titles.stream()
                    .map(title -> String.valueOf(Collections.frequency(uniqueClass, title)))
                    .collect(Collectors.joining(splitMark));
            csvContent = csvContent.concat(content);
            writeLine(csvFile,csvContent);
        }
    }

}
