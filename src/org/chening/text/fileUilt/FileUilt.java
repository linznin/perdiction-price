package org.chening.text.fileUilt;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by linznin on 2017/8/3.
 */
public class FileUilt {

    public void writeLine(File file, String line) {
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.write(line+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(File file){
        String fileLine ="";
        byte[] buf = new byte[4096];
        try {
            FileInputStream fis = new FileInputStream(file);
            int len;
            while( (len=fis.read(buf,0,buf.length)) != -1) {
                String charset_ = detectEncode(buf, len);
                String line = new String(buf,0,len, charset_);
                fileLine = fileLine.concat(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileLine;
    }

    private String detectEncode(byte[] buf, int len){
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(buf, 0, len);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }
}
