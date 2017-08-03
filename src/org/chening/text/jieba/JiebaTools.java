package org.chening.text.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.chening.text.core.ProSetting;
import org.chening.text.fileUilt.FileUilt;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by linznin on 2017/2/15.
 */
public class JiebaTools {
    private static JiebaSegmenter segmenter = new JiebaSegmenter();

    public void jiebaSeq(){
        FileUilt fileUilt = new FileUilt();
        File file = new File(ProSetting.ORG_PATH);
        ArrayList<String> jiebaResult = segText(fileUilt.readFile(file));
        fileUilt.writeLine(new File(ProSetting.RESULT_PATH+"/"+file.getName()),String.join(" ",jiebaResult));
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
