package org.chening.text.lda;

import org.chening.text.semantic.SemanticUilt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by linznin on 2017/7/8.
 */
public class LdaSearch {

    public void searchLdaFile(){
        File ldaFile = new File("/Users/linznin/tmp/LDA/result/model-final.tassign");
        File textFile = new File("/Users/linznin/tmp/LDA/result/topicCount");
        SemanticUilt semanticUilt = new SemanticUilt();
        HashMap<String,Integer>  textTopicMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(ldaFile));
            String line;
            while ((line = br.readLine()) != null) {
                int[] topices = new int[30];
                Arrays.fill(topices, 0);
                ArrayList<String> wordTopice = new ArrayList<>();
                String[] row = line.split(" ");
                for (String word : row) {
                    String[] wordt = word.split(":");
                    System.out.println(wordt[1]);
                    int topic = Integer.parseInt(wordt[1]);
                    int count  = topices[topic]+1;
                    topices[topic] = count;
                }
                String countResult = Arrays.toString(topices).replace("[", "").replace("]", "");
                semanticUilt.writeLine(textFile,countResult);

            }





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
