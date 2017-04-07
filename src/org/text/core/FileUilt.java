package org.text.core;

import org.chening.text.core.Constants;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by linznin on 2017/2/16.
 */
public class FileUilt implements TextConstants,Constants {

    protected HashMap<String,ArrayList<String>> dicMach;

    protected String charset_ = new String();

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
        ArrayList<String> wordList = analysis(text);
        ArrayList<String> fileClasses = new ArrayList<>();
        ArrayList<String> fileWords = new ArrayList<>();
        for (String word: wordList){
            for (String key: dicMach.keySet()) {
                if (dicMach.get(key).contains(word)){
                    fileClasses.add(key);
                    fileWords.add(word);
                }
            }
        }
        return fileClasses;
    }


    public String detectEncode(byte [] buf, int len){
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(buf, 0, len);
        detector.dataEnd();
        charset_ = detector.getDetectedCharset();
        return charset_;
    }

    public ArrayList<String> readFile(File file) {
        byte[] buf = new byte[4096];
        ArrayList<String> fileClasses = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            int len;
            String fileLine = "";
            while( (len=fis.read(buf,0,buf.length)) != -1) {
                detectEncode(buf, len);
                String line = new String(buf,0,len, charset_);
                fileLine = fileLine.concat(line);
            }
            if (!"".equals(fileLine)) {
                Charset.forName("UTF-8").encode(fileLine);
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
