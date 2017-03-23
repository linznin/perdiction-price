package org.text.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by linznin on 2017/3/13.
 */
public class ResultCsv {

    public static void main(String[] args) throws IOException {
        ResultCsv.readResult();
    }

    static String resultPath = "/Users/linznin/tmp/result";
    static String csvFile = "/Users/linznin/tmp/result.csv";

    public static void readResult() throws IOException {

        File Path = new File(resultPath);
        for (final File fileEntry : Path.listFiles()) {
            if (fileEntry.isFile() ) {
                BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                ArrayList<String> resultC = new ArrayList<>();
                String line ;
                while ((line = br.readLine()) !=null && !"".equals(line)) {
                    String[] ls = line.split(":");
                    if (ls.length>0){
                        switch (ls[0]){
                            case "Data file ":
                                resultC.add(fileEntry.getName());
                                resultC.add(ls[2].split("\\\\")[4]);
                                break;
                            case "C":
                                resultC.add(ls[1]);
                                break;
                            case "W":
                                resultC.add(ls[1]);
                                break;
                            case "gBest ":
                                resultC.add(ls[1]);
                                break;
                            case "location ":
                                resultC.add(ls[1].replaceAll(",",";"));
                                break;
                            case "time ":
                                writeCSV(resultC);
                                resultC = new ArrayList<>();
                                break;
                            default:
//                                System.out.println("");
                        }
                    }
                }

            }
        }
    }

    public static void writeCSV(ArrayList<String> resultC) throws FileNotFoundException {
        File csv = new File(csvFile);
        try (FileWriter writer = new FileWriter(csv, true))
        {
            writer.append(resultC.toString()+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
