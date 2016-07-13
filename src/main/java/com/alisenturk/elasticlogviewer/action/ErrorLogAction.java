package com.alisenturk.elasticlogviewer.action;

import com.alisenturk.elasticlogviewer.dao.ErrorLogDAO;
import com.alisenturk.elasticlogviewer.entity.ErrorClass;
import com.alisenturk.elasticlogviewer.entity.ErrorSearchCriteria;
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
public class ErrorLogAction implements Serializable{
    
    @Inject
    ErrorLogDAO errorLogDAO;
    
    private ErrorSearchCriteria criteria = new ErrorSearchCriteria();
    
    private List<String>        projectNames    = new ArrayList<>();
    private List<ErrorClass>    errorList       = new ArrayList<>();
    private ErrorClass          instance;
    
    @PostConstruct
    private void init(){
        criteria.setEndDate(new Date());
        criteria.setBeginDate(Helper.dateAddMinute(criteria.getEndDate(),-30));
    }


    public void searchLog(){
        errorList.clear();
        errorLogDAO.setCriteria(criteria);
        errorList.addAll(errorLogDAO.getErrorLogs());
    }
    
    public List<String> getProjectNames() {
        if(projectNames.isEmpty()){
            projectNames.addAll(errorLogDAO.getProjectList());
        }
        return projectNames;
    }

    public void setProjectNames(List<String> projectNames) {
        this.projectNames = projectNames;
    }

    public List<ErrorClass> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ErrorClass> errorList) {
        this.errorList = errorList;
    }

    public ErrorSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(ErrorSearchCriteria criteria) {
        this.criteria = criteria;
    }

    public ErrorClass getInstance() {
        return instance;
    }

    public void setInstance(ErrorClass instance) {
        this.instance = instance;
    }
    
    
    
    
}
