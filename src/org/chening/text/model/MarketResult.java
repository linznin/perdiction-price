package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by linznin on 2017/3/30.
 */
public class MarketResult {

    public MarketResult(){}

    public MarketResult(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setDate(rs.getString("date"));
        this.setRiseValue(rs.getString("rise_value"));
        this.setHasRise(rs.getBoolean("has_rise"));
        this.setLdaId(rs.getInt("lda_id"));
    }

    private int id;

    private String date;

    private boolean hasRise;

    private int ldaId;

    private String riseValue;

    public int getLdaId() {
        return ldaId;
    }

    public void setLdaId(int ldaId) {
        this.ldaId = ldaId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHasRise() {
        return hasRise;
    }

    public void setHasRise(boolean hasRise) {
        this.hasRise = hasRise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRiseValue() {
        return riseValue;
    }

    public void setRiseValue(String riseValue) {
        this.riseValue = riseValue;
    }
}
