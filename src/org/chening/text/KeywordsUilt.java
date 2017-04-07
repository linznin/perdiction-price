package org.chening.text;

import org.text.core.FileUilt;
import org.text.dic.SegDictionary;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by linznin on 2017/2/3.
 */
public class KeywordsUilt extends FileUilt {

    public void execute() {
        SegDictionary segDictionary = new SegDictionary();
        segDictionary.setDicFile(new File(CLIWC_DICFILE));
        ArrayList<String> classTitle = new ArrayList<>(Arrays.asList(targetFeatrue));
        dicMach = segDictionary.getDicClass();

        HashMap<String,ArrayList<String>> filesClasses = scanFolder( new File(KEYWORD_PATH),"dat");
        // csv title
        csvTitle(classTitle,new File(SEMANTIC_FILE));
        // csv content
        csvContent(filesClasses,new ArrayList<>(dicMach.keySet()),new File(SEMANTIC_FILE));
    }

    public void csvTitle(ArrayList<String> titles, File csvFile) {
        String splitMark = ", ";
        String csvTitle = "FileName".concat(splitMark);
        String title = titles.stream()
                .collect(Collectors.joining(splitMark));
        csvTitle = csvTitle.concat(title);
        writeLine(csvFile,csvTitle);
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
