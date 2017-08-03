package org.chening.text.lda;


import jgibblda.LDA;

public class LdaDrive {

    public static void main(String args[]){
        new LdaDrive().executeLDA();
        new LdaSearch().searchLdaFile();
    }

    public void executeLDA(){
        LDA lda = new LDA();
        String[] args = { "-est",
                "-niters", "2000",
                "-beta", "0.1",
                "-alpha", "0.5",
                "-ntopics", "30",
                "-dir", "/Users/linznin/tmp/LDA/result",
                "-dfile", "food_train.dat",
        };

        lda.main(args);
        System.out.print("is end");

    }


}
