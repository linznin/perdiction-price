package org.chening.text.dao;

/**
 * Created by linznin on 2017/3/30.
 */
import org.chening.text.model.*;

import java.sql.*;
import java.util.ArrayList;

public class LdaDao {
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String DB_NAME = "text_mining";
    private static final  String CONNECT_SETTING ="?characterEncoding=utf8";

    Connection conn = DriverManager.getConnection(DB_URL+DB_NAME+CONNECT_SETTING,"root","a12345");

    Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, Statement.RETURN_GENERATED_KEYS);

    public LdaDao() throws SQLException {}

    public int createLda(Lda lda){
        int ldaFileId = lda.getLdaFileId();
        int fileType = getNewsType(lda.getFileType()).getId();
        int topicCount = lda.getTopicCount();
        int index = lda.getIndex();
        String sql = "INSERT INTO text_mining.lda (lda_file_id, file_type, topic_count, lda_index) VALUES( ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1,ldaFileId);
            pstmt.setInt(2,fileType);
            pstmt.setInt(3, topicCount);
            pstmt.setInt(4, index);
            if (pstmt.executeUpdate() > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createLdaFile(LdaFile ldaFile){
        String fileName = ldaFile.getFileName();
        String fileTarget = ldaFile.getFileTarget();
        String sql = "INSERT INTO text_mining.lda_file (file_name, file_target) VALUES( ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,fileName);
            pstmt.setString(2,fileTarget);
            if (pstmt.executeUpdate() > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void createMarketResult(MarketResult marketResult){
        String date = marketResult.getDate();
        String riseValue = marketResult.getRiseValue();
        boolean hasRise = marketResult.isHasRise();
        int ldaId = marketResult.getLdaId();
        String sql = "INSERT INTO market_result (date, has_rise, lda_id, rise_value) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,date);
            pstmt.setBoolean(2,hasRise);
            pstmt.setInt(3,ldaId);
            pstmt.setString(4,riseValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createLdaTopic(LdaTopic ldaTopic){
        String topicValue = ldaTopic.getTopicValue();
        int topicIndex = ldaTopic.getTopicIndex();
        int ldaId = ldaTopic.getLdaId();
        String sql = "INSERT INTO lda_topic (topic_index , topic_value, lda_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1,topicIndex);
            pstmt.setString(2,topicValue);
            pstmt.setInt(3,ldaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int createNewsType(String type){
        String sql = "INSERT INTO text_mining.news_type (type) VALUES(?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,type);
            if (pstmt.executeUpdate() > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createSemanticType(SemanticType semanticType){
        return this.createSemanticType(semanticType.getSemanticType());
    }

    public int createSemanticType(String semanticType){
        String sql = "INSERT INTO text_mining.semantic_type (semantic_type) VALUES(?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,semanticType);
            if (pstmt.executeUpdate() > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createSemantic(Semantic semantic){
        String type = semantic.getSemanticType();
        int ldaId = semantic.getLdaId();
        int semanticCount =semantic.getSemanticCount();
        String sql = "INSERT INTO text_mining.semantic (semantic_count, semantic_type_id, lda_id) VALUES(?, ?, ?)";
        try {
            int semanticTypeId = this.getSemanticType(type).getId();
            if (semanticTypeId > -1){
                PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1,semanticCount);
                pstmt.setInt(2,semanticTypeId);
                pstmt.setInt(3,ldaId);
                if (pstmt.executeUpdate() > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Lda getLda(int id){
        Lda lda = null;
        String sql = "SELECT t.*, f.file_name, f.file_target FROM text_mining.lda t join text_mining.lda_file f AS f.lda_id = t.id"+
                " WHERE t.id = "+id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                lda = new Lda(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lda;
    }

    public Lda getLda(Lda condition){
        Lda lda = null;
        String sql = "SELECT t.id,t.lda_file_id, n.type AS file_type, f.file_name, f.file_target FROM text_mining.lda as t "+
                " join text_mining.lda_file as f ON f.id = t.lda_file_id"+
                " JOIN text_mining.news_type as n ON t.file_type = n.id"+
                " WHERE file_name = '"+condition.getFileName()+"'"+
                " AND file_target = '"+condition.getFileTarget()+"'"+
                " AND topic_count = '"+condition.getTopicCount()+"'"+
                " AND n.type = '"+condition.getFileType()+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                lda = new Lda(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lda;
    }

    public ArrayList<Lda> getLdas(Lda condition){

        ArrayList<Lda> lda = new ArrayList<>();
        String sql = "SELECT t.id,t.lda_file_id, n.type  AS file_type, f.file_name, f.file_target, t.topic_count FROM text_mining.lda t "+
                " join text_mining.lda_file f ON f.id = t.lda_file_id"+
                " join text_mining.news_type n ON t.file_type = n.id"+
                " WHERE type = '"+condition.getFileType()+"'"+
                " AND topic_count = '"+condition.getTopicCount()+"'"+
                " ORDER BY lda_index";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                lda.add(new Lda(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lda;
    }

    public Lda getLda(String fileTarget){
        Lda lda = null;
        String sql = "SELECT t.id, n.type AS file_type, f.file_name, f.file_target FROM text_mining.lda t "+
                " join text_mining.lda_file f ON f.id = t.lda_file_id"+
                " join text_mining.news_type n ON t.file_type = n.id"+
                " WHERE file_target = '"+fileTarget+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                lda = new Lda(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lda;
    }

    public LdaFile getLdaFile(String fileName, String fileTarget){
        LdaFile ldaFile = null;
        String sql = "SELECT t.* FROM text_mining.lda_file t WHERE file_name = '"+fileName+
                "' and file_target = '"+fileTarget+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaFile = new LdaFile(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaFile;
    }

    public LdaFile getLdaFile(String fileTarget){
        LdaFile ldaFile = null;
        String sql = "SELECT t.* FROM text_mining.lda_file t WHERE file_target = '"+fileTarget+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaFile = new LdaFile(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaFile;
    }

    public LdaFile getLdaFile(LdaFile condition){
        LdaFile ldaFile = null;
        String sql = "SELECT t.* FROM text_mining.lda_file t WHERE file_name = '"+condition.getFileName()+
                "' and file_target = '"+condition.getFileTarget()+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaFile = new LdaFile(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaFile;
    }

    public ArrayList<LdaFile> getLdaFiles(){
        ArrayList<LdaFile> ldaFiles = new ArrayList<>();
        String sql = "SELECT t.* FROM text_mining.lda_file t ";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaFiles.add(new LdaFile(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaFiles;
    }

    public MarketResult getMarketResult(int ldaId){
        MarketResult ldaResult = null;
        String sql = "SELECT t.* FROM text_mining.market_result t WHERE lda_id = "+ldaId;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaResult = new MarketResult(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaResult;
    }

    public MarketResult getMarketResult(MarketResult marketResult){
        MarketResult ldaResult = null;
        int ldaId = marketResult.getLdaId();
        String date = marketResult.getDate();
        boolean hasRise = marketResult.isHasRise();
        String riseValue = marketResult.getRiseValue();
        String sql = "SELECT t.* FROM text_mining.market_result t WHERE lda_id = "+ldaId+
                " AND date = " +date + " AND has_rise = "+hasRise + " AND rise_value = "+riseValue;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaResult = new MarketResult(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaResult;
    }

    public ArrayList<MarketResult> getMarketResults(int ldaId){
        ArrayList<MarketResult> ldaResults = new ArrayList<>();
        String sql = "SELECT t.* FROM text_mining.market_result t WHERE lda_id = "+ldaId;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaResults.add(new MarketResult(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaResults;
    }

    public LdaTopic getLdaTopic(int id){
        LdaTopic ldaTopic = null;
        String sql = "SELECT t.* FROM text_mining.lda_topic t WHERE id = "+id;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaTopic = new LdaTopic(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaTopic;
    }

    public LdaTopic getLdaTopic(LdaTopic ldaTopic){
        LdaTopic topic = null;
        int ldaId = ldaTopic.getLdaId();
        int topicIndex = ldaTopic.getTopicIndex();
        String topicValue = ldaTopic.getTopicValue();
        String sql = "SELECT * FROM text_mining.lda_topic t WHERE t.lda_id = "+ldaId+
                " AND t.topic_index = "+topicIndex+
                " AND t.topic_value = '"+topicValue+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                topic = new LdaTopic(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topic;
    }

    public ArrayList<LdaTopic> getLdaTopics(int ldaId){
        ArrayList<LdaTopic> ldaTopics = new ArrayList<>();
        String sql = "SELECT t.* FROM text_mining.lda_topic t WHERE lda_id = "+ldaId +" ORDER BY topic_index";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                ldaTopics.add(new LdaTopic(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ldaTopics;
    }

    public ArrayList<Semantic> getSemantics(int ldaId) {
        ArrayList<Semantic> semantics = new ArrayList<>();
        String sql = "SELECT s.id, s.semantic_count, s.lda_id, t.semantic_type FROM text_mining.semantic s JOIN text_mining.semantic_type t ON s.semantic_type_id = t.id"+
                " WHERE lda_id = "+ldaId +" ORDER BY semantic_type_id";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                semantics.add(new Semantic(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semantics;
    }

    public Semantic getSemantic(Semantic condition) {
        Semantic semantic = null;
        int ldaId = condition.getLdaId();
        String semanticType = condition.getSemanticType();
        String sql = "SELECT s.id, s.semantic_count, s.lda_id, t.semantic_type FROM text_mining.semantic s JOIN text_mining.semantic_type t ON s.semantic_type_id = t.id"+
                " WHERE lda_id = "+ldaId+
                " AND t.semantic_type = '"+semanticType+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                semantic = new Semantic(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semantic;
    }

    public SemanticType getSemanticType(String type){
        SemanticType semanticType = null;
        String sql = "SELECT t.* FROM text_mining.semantic_type t WHERE t.semantic_type = '"+type+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                semanticType = new SemanticType(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return semanticType;
    }

    public NewsType getNewsType(String type){
        NewsType newsType = null;
        String sql = "SELECT t.* FROM text_mining.news_type t WHERE t.type = '"+type+"'";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                newsType = new NewsType(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newsType;
    }

    public ArrayList<NewsType> getNewsTypes() {
        ArrayList<NewsType> newsTypes = new ArrayList<>();
        String sql = "SELECT * FROM text_mining.news_type";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                newsTypes.add(new NewsType(rs));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newsTypes;
    }
}
