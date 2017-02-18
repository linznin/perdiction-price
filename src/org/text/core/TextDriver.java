package org.text.core;

import org.chening.text.KeywordsUilt;

/**
 * Created by linznin on 2017/2/17.
 */
public class TextDriver implements FileConstants{

    public static void main(String args[]) {
        JibaUilt uilt = new JibaUilt();
//        KeywordsUilt uilt = new KeywordsUilt();
        uilt.execute();

    }
}
