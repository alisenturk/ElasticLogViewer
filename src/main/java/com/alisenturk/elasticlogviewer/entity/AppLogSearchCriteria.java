package com.alisenturk.elasticlogviewer.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author alisenturk
 */
public class AppLogSearchCriteria  implements Serializable{

    private String  projectName;
    private String  serviceName;
    private String  transactionId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    
    
}
