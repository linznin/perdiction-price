package org.chening.text;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by linznin on 2017/2/3.
 */
public class FileUil {

    HashMap<String,ArrayList<String>> dicMach;

    public static void main(String args[]) {

        String dicPath = "/Users/linznin/Documents/study/Paper/cheninglin/中文情緒字典/cliwc_v1.2222.txt";
        String textPath = "/Users/linznin/Documents/study/Paper/cheninglin/data";
        String csvFile = "/Users/linznin/Desktop/csvtest.csv";

        FileUil fileUil = new FileUil();
        SegDictionary segDictionary = new SegDictionary();
        segDictionary.setDicFile(new File(dicPath));
        ArrayList<String> classTitle = new ArrayList<>();
        classTitle.add("negemo");
        classTitle.add("anx");
        classTitle.add("sad");
        classTitle.add("cogmech");
        classTitle.add("affect");
        classTitle.add("posemo");
        fileUil.dicMach = segDictionary.getDicClass(classTitle);


        HashMap<String,ArrayList<String>> filesClasses = fileUil.scanFolder(new File(textPath));

        // csv title
        fileUil.csvTitle(classTitle,new File(csvFile));
        // csv content
        fileUil.csvContent(filesClasses,new ArrayList<>(fileUil.dicMach.keySet()),new File(csvFile));
    }

    private void csvTitle(ArrayList<String> titles, File csvFile) {
        String splitMark = ", ";
        String csvTitle = "FileName".concat(splitMark);
        String title = titles.stream()
                .collect(Collectors.joining(splitMark));
        csvTitle = csvTitle.concat(title);
        writeLine(csvFile,csvTitle);
    }

    public ArrayList<String> readFile(File file) {
        ArrayList<String> fileClasses = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) !=null) {
                if (lineCount >= 1) {
                    fileClasses.addAll(analyFile(line,file.getName()));
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileClasses;
    }

    public HashMap<String,ArrayList<String>> scanFolder(File Path) {
        HashMap<String,ArrayList<String>> filesClasses = new HashMap<>();
        for (final File fileEntry : Path.listFiles()) {
            if (fileEntry.isDirectory()) {
                filesClasses.putAll(scanFolder(fileEntry));
            } else {
                // 取得副檔名
                int startIndex = fileEntry.getName().lastIndexOf(46) + 1;
                int endIndex = fileEntry.getName().length();
                if (fileEntry.getName().substring(startIndex, endIndex).equals("dat")) {
                    filesClasses.put(fileEntry.getName(),readFile(fileEntry));
                }
            }
        }
        return filesClasses;
    }

    public ArrayList<String> analyFile(String text,String fileName) {
        String[] feature = text.split("\\s+");
        ArrayList<String> fileClasses = new ArrayList<>();
        ArrayList<String> fileWords = new ArrayList<>();
        for (int i =0;i<feature.length;i++){
            for (String key: dicMach.keySet()) {
                if (dicMach.get(key).contains(feature[i])){
                    fileClasses.add(key);
                    fileWords.add(feature[i]);
                }
            }
        }

        return fileClasses;
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

    public void writeLine(File csvFile, String line) {
        try {
            FileWriter writer = new FileWriter(csvFile,true);
            writer.write(line+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
