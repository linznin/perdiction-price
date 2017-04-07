package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by linznin on 2017/3/31.
 */
public class Semantic {

    public Semantic(){}

    public Semantic(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setLdaId(rs.getInt("lda_id"));
        this.setSemanticCount(rs.getInt("semantic_count"));
        this.setSemanticType(rs.getString("semantic_type"));
    }

    private int id;

    private int semanticCount;

    private String semanticType;

    private int ldaId;

    public int getLdaId() {
        return ldaId;
    }

    public void setLdaId(int ldaId) {
        this.ldaId = ldaId;
    }

    public String getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    public int getSemanticCount() {
        return semanticCount;
    }

    public void setSemanticCount(int semanticCount) {
        this.semanticCount = semanticCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
