package com.alisenturk.elasticlogviewer.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author alisenturk
 */
public class ErrorSearchCriteria  implements Serializable{

    private String  projectName;
    private Date    beginDate;
    private Date    endDate;
    private String  keyword;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    
    
}
