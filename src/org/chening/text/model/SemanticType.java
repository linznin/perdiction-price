package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by linznin on 2017/3/31.
 */
public class SemanticType {

    public SemanticType(){}

    public SemanticType(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setSemanticType(rs.getString("semantic_type"));
    }

    private int id;

    private String semanticType;

    public String getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
