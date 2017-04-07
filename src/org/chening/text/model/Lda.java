package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 */
public class Lda {

    public Lda(){}

    public Lda (ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setFileName(rs.getString("file_name"));
        this.setFileTarget(rs.getString("file_target"));
        this.setFileType(rs.getString("file_type"));
        this.setLdaFileId(rs.getInt("lda_file_id"));
        this.setTopicCount(rs.getInt("topic_count"));
    }

    private int id;

    private String fileType;

    private int ldaFileId;

    private String fileName;

    private String fileTarget;

    private int topicCount;

    private boolean hasRise;

    private int index;

    private ArrayList<LdaTopic> ldaTopics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTarget() {
        return fileTarget;
    }

    public void setFileTarget(String fileTarget) {
        this.fileTarget = fileTarget;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }


    public boolean isHasRise() {
        return hasRise;
    }

    public void setHasRise(boolean hasRise) {
        this.hasRise = hasRise;
    }

    public ArrayList<LdaTopic> getLdaTopics() {
        return ldaTopics;
    }

    public void setLdaTopics(ArrayList<LdaTopic> ldaTopics) {
        this.ldaTopics = ldaTopics;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getLdaFileId() {
        return ldaFileId;
    }

    public void setLdaFileId(int ldaFileId) {
        this.ldaFileId = ldaFileId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
