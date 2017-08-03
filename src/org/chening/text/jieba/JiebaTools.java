package org.chening.text.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.ArrayList;

/**
 * Created by linznin on 2017/2/15.
 */
public class JiebaTools {
    static JiebaSegmenter segmenter = new JiebaSegmenter();

    public static void main(String[] args) {
        JiebaTools jiebaTools = new JiebaTools();
        String s = "而EMS方面，由於客戶進入旺季拉貨時間，因此出貨動能也逐步加溫，第3季營收估增1成以上，推升整體合併營收季增15%以上，以日月光第3季結果來看，表現也在預期區間內。";
        ArrayList sq = jiebaTools.segText(s);
        System.out.println("sq = [" + sq + "]");
    }

    public ArrayList<String> segText(String content){
        ArrayList<String> seqWordList = new ArrayList<>();
        // https://github.com/huaban/jieba-analysis
        for (SegToken segToken: segmenter.process(content, JiebaSegmenter.SegMode.SEARCH)) {
            seqWordList.add(segToken.word);
        }
        return seqWordList;
    }
}
