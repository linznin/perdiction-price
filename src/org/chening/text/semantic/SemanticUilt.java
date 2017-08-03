package org.chening.text.semantic;

import org.chening.text.core.ProSetting;
import org.chening.text.dic.CliwcDictionary;
import org.chening.text.fileUilt.FileUilt;
import org.chening.text.jieba.JiebaTools;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by linznin on 2017/2/16.
 */
public class SemanticUilt implements SemanticConstants {

    protected HashMap<String,ArrayList<String>> dicMach;
    protected FileUilt fileUilt = new FileUilt();


    public void execute() {
        System.out.println(ProSetting.OPTIMIZATION_FUNCTION);
        CliwcDictionary cliwcDictionary = new CliwcDictionary(new File(ProSetting.CLIWC_DICFILE));
        dicMach = cliwcDictionary.getDicClass();
        HashMap<String,ArrayList<String>> filesSemanticClasses = scanFolder( new File(ProSetting.NEWS_PATH),"txt");

        // csv title
        csvTitle(cliwcDictionary.getTitle(),new File(ProSetting.SEMANTIC_RESULT_FILE));
        // csv content
        csvContent(filesSemanticClasses,cliwcDictionary.getKeySet(),new File(ProSetting.SEMANTIC_RESULT_FILE));
    }

    private HashMap<String,ArrayList<String>> scanFolder(File Path, String fileType) {
        HashMap<String,ArrayList<String>> filesClasses = new HashMap<>();
        for (final File fileEntry : Path.listFiles()) {
            if (fileEntry.isDirectory()) {
                filesClasses.putAll(scanFolder(fileEntry, fileType));
            } else {
                // 取得副檔名
                int startIndex = fileEntry.getName().lastIndexOf(46) + 1;
                int endIndex = fileEntry.getName().length();
                if (fileEntry.getName().substring(startIndex, endIndex).equals(fileType)) {
                    filesClasses.put(fileEntry.getName(), getFileSemantic(fileEntry));
                }
            }
        }
        return filesClasses;
    }

    private ArrayList<String> getWords(String text) {
        JiebaTools jiebaTools = new JiebaTools();
        ArrayList<String> result;
        try {
            result = jiebaTools.segText(text);
        } catch (Exception e) {
            result = new ArrayList<>();
        }
        return result;
    }

    private ArrayList<String> getTextSemantic(String text) {
        ArrayList<String> wordList = getWords(text);
        ArrayList<String> fileClasses = new ArrayList<>();
        for (String word: wordList){
            for (String key: dicMach.keySet()) {
                if (dicMach.get(key).contains(word)){
                    fileClasses.add(key);
                }
            }
        }
        return fileClasses;
    }

    private ArrayList<String> getSemanticWords(String text) {
        ArrayList<String> wordList = getWords(text);
        ArrayList<String> fileWords = new ArrayList<>();
        for (String word: wordList){
            for (String key: dicMach.keySet()) {
                if (dicMach.get(key).contains(word)){
                    fileWords.add(word);
                }
            }
        }
        return fileWords;
    }


    private ArrayList<String> getFileSemantic(File file) {
        ArrayList<String> fileClasses = new ArrayList<>();
        String fileLine = fileUilt.readFile(file);
        if (!"".equals(fileLine)) {
            Charset.forName("UTF-8").encode(fileLine);
            fileClasses.addAll(getTextSemantic(fileLine));
            if (ProSetting.KEEP_JIEBA_RESULT) {
                fileUilt.writeLine( new File(ProSetting.JIEBA_RESULT_PATH+file.getName()) , String.join(" ",getWords(fileLine)));
                fileUilt.writeLine( new File(ProSetting.JIEBA_RESULT_PATH+file.getName()) , String.join(" ",fileClasses));
            }
        }
        return fileClasses;
    }

    private ArrayList<String> getFileSemanticWords(File file) {
        ArrayList<String> fileWords = new ArrayList<>();
        String fileLine = fileUilt.readFile(file);
        if (!"".equals(fileLine)) {
            Charset.forName("UTF-8").encode(fileLine);
            fileWords.addAll(getSemanticWords(fileLine));
        }
        return fileWords;
    }



    private void mkResultPreview(){
        File newsFile = new File("/Users/linznin/tmp/20140909-091956483530010-美國掀起穿戴攝影風潮，GoPro、安霸創歷史收盤新高.txt");
        File resultPreview = new File("/Users/linznin/tmp/resultPreview");
        ArrayList<String> fileWords = getFileSemanticWords(newsFile);
        ArrayList<String> fileClasses = getFileSemantic(newsFile);
        ArrayList<String> word = getWords(fileUilt.readFile(newsFile));
        fileUilt.writeLine(resultPreview,String.join(" ",fileWords));
        fileUilt.writeLine(resultPreview,String.join(" ",fileClasses));
        System.out.println("size is "+fileClasses.size());
    }



    private void csvTitle(ArrayList<String> titles, File csvFile) {
        String splitMark = ",";
        String csvTitle = "FileName".concat(splitMark);

        for (String title : titles){
            csvTitle = csvTitle.concat(title).concat(splitMark);
        }

        csvTitle = csvTitle.substring(0,csvTitle.length()-1);
        fileUilt.writeLine(csvFile,csvTitle);
    }

    private HashMap<String, HashMap<String,String>> csvContent(HashMap<String, ArrayList<String>> filesClasses, ArrayList<String> titles, File csvFile) {
        HashMap<String,HashMap<String, String>> tmp = new HashMap<>();
        String splitMark = ", ";
        for (String fileName : filesClasses.keySet()) {
            String content = fileName.replaceAll(",", "").replace(".txt","");
            ArrayList<String> uniqueClass = filesClasses.get(fileName);
            HashMap<String, String> frequencymap = new HashMap<>();
            for (String title : titles) {
                Integer featureCount = 0;
                for (String wordFeature : uniqueClass) {
                    if (title.equals(wordFeature)) {
                        featureCount++;
                    }
                }
                frequencymap.put(title,featureCount.toString());
                content = content.concat(splitMark).concat(featureCount.toString());
                tmp.put(fileName, frequencymap);
            }
            fileUilt.writeLine(csvFile, content);
        }

        return tmp;
    }


}
