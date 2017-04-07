package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by linznin on 2017/4/4.
 */
public class NewsType {

    public NewsType(){}

    public NewsType(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setType(rs.getString("type"));
    }

    private int id;

    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
