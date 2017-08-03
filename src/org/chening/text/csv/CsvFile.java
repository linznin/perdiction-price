package org.chening.text.csv;

import org.chening.text.core.ProSetting;
import org.chening.text.dao.LdaDao;
import org.chening.text.model.*;
import org.chening.text.semantic.SemanticUilt;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CsvFile {

    SemanticUilt semanticUilt = new SemanticUilt();

    public String genRowData(String path,String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(path+fileName)))
        {
            String rowData = "";
            PrintWriter writer = new PrintWriter(path+fileName+"train", "UTF-8");
            //依行讀入資料
            String line = "";
            while ((line = br.readLine()) != null) {
                //依照空白分割 lable
                String[] row = line.split(",");
                rowData = row[0];
                for (int i = 1; i < row.length; i++) {
                    rowData += " "+i+":"+row[i];
                }
                System.out.println(rowData);
                writer.println(rowData);
                rowData = "";
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void csvtodb(File path){
        try {
            LdaDao ldaDao = new LdaDao();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path),"utf8"));
            //依行讀入資料
            String line = br.readLine();
            int index =1;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String fileName = row[0];
                String fileTarget = fileName.replace(" ","").substring(0,24);
                LdaFile ldaFile = new LdaFile();
                ldaFile.setFileName(fileName);
                ldaFile.setFileTarget(fileTarget);
                int ldaFileId;
                LdaFile searchLdaFile;
                if ((searchLdaFile=ldaDao.getLdaFile(ldaFile)) != null){
                    ldaFileId = searchLdaFile.getId();
                } else {
                    ldaFileId = ldaDao.createLdaFile(ldaFile);
                }
                if (ldaDao.getNewsType(path.getParentFile().getName()) == null ) {
                    ldaDao.createNewsType(path.getParentFile().getName());
                }
                Lda lda = new Lda();
                lda.setFileType(path.getParentFile().getName());
                lda.setLdaFileId(ldaFileId);
                lda.setTopicCount(row.length-4);
                lda.setIndex(index);
                int ldaId = ldaDao.createLda(lda);

                for (int i = 1; i < row.length-3; i++) {
                    LdaTopic ldaTopic = new LdaTopic();
                    ldaTopic.setTopicIndex(i-1);
                    ldaTopic.setLdaId(ldaId);
                    ldaTopic.setTopicValue(row[i]);
                    if (ldaDao.getLdaTopic(ldaTopic) == null) {
                        ldaDao.createLdaTopic(ldaTopic);
                    }
                }

                String dates[] = {"1","3","5"};
                for (int i = 0; i < 3; i++) {
                    String riseValue = row[row.length-(3-i)];
                    MarketResult marketResult = new MarketResult();
                    marketResult.setLdaId(ldaId);
                    marketResult.setDate(dates[i]);
                    marketResult.setRiseValue(riseValue);
                    marketResult.setHasRise(Double.valueOf(riseValue) > 0);
                    if (ldaDao.getMarketResult(marketResult) == null ) {
                        ldaDao.createMarketResult(marketResult);
                    }
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sematictodb(){
        File org_file = new File(ProSetting.SEMANTIC_RESULT_FILE);
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(org_file));
            LdaDao ldaDao = new LdaDao();
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                String fileName = row[0];
                String fileTarget = row[1];
                LdaFile ldaFile = ldaDao.getLdaFile(fileTarget);

                if (ldaFile!=null){
                    int ldaId = ldaFile.getId();
                    String[] semanticTag = {"posemo","negemo","affet","sad","cogmech","anx","anger"};
                    for (String tag : semanticTag) {
                        if (ldaDao.getSemanticType(tag) == null) {
                            ldaDao.createSemanticType(tag);
                        }
                    }
                    for (int i = 2; i < row.length-3; i++) {
                        Semantic semantic = new Semantic();
                        semantic.setSemanticCount(Integer.valueOf(row[i]));
                        semantic.setLdaId(ldaId);
                        semantic.setSemanticType(semanticTag[i-2]);
                        if (ldaDao.getSemantic(semantic) == null){
                            ldaDao.createSemantic(semantic);
                        }
                    }
                } else {
                    System.out.println("file name: "+fileName);
                    System.out.println("file target: "+fileTarget);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void genSemanticData(){
        String[] date = {"1","3","5"};
        File org_file = new File(ProSetting.SEMANTIC_RESULT_FILE);
        for (int j=0;j < date.length; j++){
            String trainData = ProSetting.TRAIN_PATH+org_file.getName()+date[j]+"d";
            try (BufferedReader br = new BufferedReader(new FileReader(org_file)))
            {
                String rowData = "";
                PrintWriter writer = new PrintWriter(trainData, "UTF-8");
                String line = br.readLine();
                while ((line = br.readLine()) != null) {
                    //依照空白分割 lable
                    String[] row = line.split(",");

                    if ( Double.valueOf(row[row.length+(j-3)]) > 0 )
                        rowData = "1";
                    else
                        rowData = "-1";

                    for (int i = 2; i < row.length-3; i++) {
                        rowData += " "+(i-1)+":"+row[i];
                    }
                    System.out.println(rowData);
                    writer.println(rowData);
                    rowData = "";
                }
                writer.println(rowData);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void genCSVResult(String fileName, String correct, String location, String c, String w){
        ArrayList<String> result = new ArrayList<>();
        result.add(fileName);
        result.add(c);
        result.add((w));
        result.add(correct);
        result.add(location);
        semanticUilt.writeLine(new File(ProSetting.PSO_RESULT_CSV),result.toString());
    }

    public void genCSVmapping(){
        String[] features = {"30", "75", "120"};
        LdaDao ldaDao = null;
        try {
            ldaDao = new LdaDao();
            ArrayList<NewsType> newsTypes = ldaDao.getNewsTypes();
            for (NewsType newsType : newsTypes) {
                for (String feature: features) {
                    Lda condition = new Lda();
                    condition.setTopicCount(Integer.valueOf(feature));
                    condition.setFileType(newsType.getType());
                    ArrayList<Lda> ldas = ldaDao.getLdas(condition);
                    for (Lda lda : ldas) {
                        File file = new File(ProSetting.SEMANTIC_PATH+"semanticF"+feature+"T"+lda.getFileType()+".csv");
                        FileWriter writer = new FileWriter(file,true);
                        ArrayList<String> row = new ArrayList<>();
                        row.add(lda.getFileName());
                        row.add(lda.getFileTarget());

                        ArrayList<LdaTopic> ldaTopics = ldaDao.getLdaTopics(lda.getId());
                        for (LdaTopic ldaTopic : ldaTopics) {
                            row.add(ldaTopic.getTopicValue());
                        }
                        ArrayList<Semantic> semantics = ldaDao.getSemantics(lda.getLdaFileId());
                        for (Semantic semantic: semantics) {
                            row.add( String.valueOf(semantic.getSemanticCount()) );
                        }

                        ArrayList<String> marketRow = new ArrayList<>();
                        ArrayList<MarketResult> marketResults = ldaDao.getMarketResults(lda.getId());
                        for (MarketResult marketResult: marketResults) {
                            row.add(marketResult.isHasRise()?"1":"-1");
                        }

    //                        String rowData = String.join(",",row)+","+String.join(",",marketRow);
                        String rowData = String.join(",",row);
                        System.out.println("rowData : "+rowData);
                        writer.write(rowData+"\n");
                        writer.flush();
                        writer.close();

                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void genData(){
        String[] features = {"30", "75", "120"};
        LdaDao ldaDao = null;
        try {
            ldaDao = new LdaDao();
            ArrayList<NewsType> newsTypes = ldaDao.getNewsTypes();
            for (NewsType newsType : newsTypes) {
                for (String feature: features) {
                    Lda condition = new Lda();
                    condition.setTopicCount(Integer.valueOf(feature));
                    condition.setFileType(newsType.getType());
                    ArrayList<Lda> ldas = ldaDao.getLdas(condition);
                    for (Lda lda : ldas) {
                        ArrayList<String> row = new ArrayList<>();
                        ArrayList<LdaTopic> ldaTopics = ldaDao.getLdaTopics(lda.getId());
                        for (LdaTopic ldaTopic : ldaTopics) {
                            row.add(ldaTopic.getTopicValue());
                        }
                        ArrayList<Semantic> semantics = ldaDao.getSemantics(lda.getLdaFileId());
                        for (Semantic semantic: semantics) {
                            row.add( String.valueOf(semantic.getSemanticCount()) );
                        }

                        ArrayList<MarketResult> marketResults = ldaDao.getMarketResults(lda.getId());
                        for (MarketResult marketResult: marketResults) {
                            File file = new File(ProSetting.SEMANTIC_PATH+feature+"/"+lda.getFileType()+"_"+feature+"_"+marketResult.getDate()+"d");
                            FileWriter writer = new FileWriter(file,true);

                            String rowData = (marketResult.isHasRise()?"1":"-1")+","+String.join(",",row);
                            System.out.println("rowData : "+rowData);
                            writer.write(rowData+"\n");
                            writer.flush();
                            writer.close();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}