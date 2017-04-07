package org.chening.text.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by linznin on 2017/3/30.
 */
public class LdaFile {

    public LdaFile(){}

    public LdaFile (ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setFileName(rs.getString("file_name"));
        this.setFileTarget(rs.getString("file_target"));
    }

    private int id;

    private String fileName;

    private String fileTarget;

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
}
