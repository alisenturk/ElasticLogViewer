package com.alisenturk.elasticlogviewer.action;

import com.alisenturk.elasticlogviewer.dao.AppLogDAO;
import com.alisenturk.elasticlogviewer.entity.AppLogData;
import com.alisenturk.elasticlogviewer.entity.AppLogSearchCriteria;
import com.alisenturk.elasticlogviewer.util.Helper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author alisenturk
 */
@ViewScoped
@Named
public class AppLogAction implements Serializable{
    
    @Inject
    AppLogDAO appLogDAO;
    
    private AppLogSearchCriteria criteria = new AppLogSearchCriteria();
    
    private List<String>        projectNames    = new ArrayList<>();
    private List<String>        serviceNames    = new ArrayList<>();
    private List<String>        transactionIds  = new ArrayList<>();
    private List<AppLogData>    appLogList      = new ArrayList<>();
    private AppLogData          instance;
    
    @PostConstruct
    private void init(){
        criteria.setEndDate(new Date());
        criteria.setBeginDate(Helper.dateAddMinute(criteria.getEndDate(),-30));
    }


    public void searchLog(){
        appLogList.clear();
        appLogDAO.setCriteria(criteria);
        appLogList.addAll(appLogDAO.getAppLogs());
    }
    
    public List<String> getProjectNames() {
        if(projectNames.isEmpty()){
            projectNames.addAll(appLogDAO.getProjectList());
        }
        return projectNames;
    }

    public void setProjectNames(List<String> projectNames) {
        this.projectNames = projectNames;
    }

    public AppLogSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(AppLogSearchCriteria criteria) {
        this.criteria = criteria;
    }

    public List<String> getServiceNames() {
        if(serviceNames.isEmpty()){
            serviceNames.addAll(appLogDAO.getServiceNametList());
        }
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public List<String> getTransactionIds() {
        if(transactionIds.isEmpty()){
            transactionIds.addAll(appLogDAO.getTransactiontList());
        }
        return transactionIds;
    }

    public void setTransactionIds(List<String> transactionIds) {
        this.transactionIds = transactionIds;
    }

    public List<AppLogData> getAppLogList() {
        return appLogList;
    }

    public void setAppLogList(List<AppLogData> appLogList) {
        this.appLogList = appLogList;
    }

    public AppLogData getInstance() {
        return instance;
    }

    public void setInstance(AppLogData instance) {
        this.instance = instance;
    }

    
    
    
}
