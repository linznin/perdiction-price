package org.text.jiba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linznin on 2017/2/15.
 */
public class JibaTools {
    static JiebaSegmenter segmenter = new JiebaSegmenter();
//    static String[] sentences = new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。", "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
    public static void main(String[] args) {
        JibaTools jibaTools = new JibaTools();
        String s = "而EMS方面，由於客戶進入旺季拉貨時間，因此出貨動能也逐步加溫，第3季營收估增1成以上，推升整體合併營收季增15%以上，以日月光第3季結果來看，表現也在預期區間內。";
        ArrayList sq = jibaTools.segText(s);
        System.out.println("sq = [" + sq + "]");

    }
    public ArrayList<String> segText(String content){
        ArrayList<String> seqWordList = new ArrayList<>();
        for (SegToken segToken: segmenter.process(content, JiebaSegmenter.SegMode.SEARCH)) {
            seqWordList.add(segToken.word);
        }
        return seqWordList;
    }
}
