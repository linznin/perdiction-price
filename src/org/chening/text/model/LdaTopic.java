package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by linznin on 2017/3/30.
 */
public class LdaTopic {

    public LdaTopic(){}

    public LdaTopic(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setTopicIndex(rs.getInt("topic_index"));
        this.setTopicValue(rs.getString("topic_value"));
        this.setLdaId(rs.getInt("lda_id"));
    }

    private int id;

    private String topicValue;

    private int topicIndex;

    private int ldaId;

    public int getLdaId() {
        return ldaId;
    }

    public void setLdaId(int ldaId) {
        this.ldaId = ldaId;
    }

    public int getTopicIndex() {
        return topicIndex;
    }

    public void setTopicIndex(int topicIndex) {
        this.topicIndex = topicIndex;
    }

    public String getTopicValue() {
        return topicValue;
    }

    public void setTopicValue(String topicValue) {
        this.topicValue = topicValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
