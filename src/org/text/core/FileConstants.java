package org.text.core;

/**
 * Created by linznin on 2017/2/17.
 */
public interface FileConstants {
    String dicPath = "/Users/linznin/tmp/dic/dictionary";
    String dicFile = "/Users/linznin/tmp/dic/cliwc_v1.txt";
    String textPath = "/Users/linznin/tmp/Keywords";
    String newsPath = "/Users/linznin/tmp/news";
    String newsSeqPath = "/Users/linznin/tmp/newsFeater";
    String resultPath = "/Users/linznin/tmp/csv/csvtest.csv";

    String[] targetFeatrue = {"posemo", "negemo", "anx", "sad", "cogmech", "affect"};
    String[] searchFile = {"ModalStrong.txt","ModalWeak.txt","Negative.txt","Postive.txt","Uncertainty.txt"};
}
