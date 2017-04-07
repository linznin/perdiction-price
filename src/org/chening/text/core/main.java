package org.chening.text.core;

import org.gandhim.csv.CsvFile;

import java.io.File;

/**
 * Created by linznin on 2017/3/25.
 */
public class main implements Constants{

    public static void main(String[] args) {
//        new main().scanFolder(new File(ORG_PATH+"/tmp"));
//        new CsvFile().sematictodb();
        new CsvFile().genData();

    }

    private static void scanFolder(File Path) {
        try {
            for (final File fileEntry : Path.listFiles()) {
                if (!fileEntry.isHidden()) {
                    if (fileEntry.isDirectory()) {
                        scanFolder(fileEntry);
                    } else {
                        System.out.println("Path = [" + fileEntry.getParentFile().getName() + "]");
                        new CsvFile().csvtodb(fileEntry);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
