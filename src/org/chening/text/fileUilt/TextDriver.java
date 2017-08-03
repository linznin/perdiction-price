package org.chening.text.fileUilt;

import org.chening.text.semantic.SemanticUilt;

/**
 * Created by linznin on 2017/2/17.
 */
public class TextDriver implements TextConstants {

    public static void main(String args[]) {
        SemanticUilt uilt = new SemanticUilt();
//        KeywordsUilt uilt = new KeywordsUilt();
//        LdaFileReader uilt = new LdaFileReader();
        uilt.execute();

    }
}
