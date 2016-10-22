package com.alisenturk.elasticlogviewer.dao;

import com.alisenturk.elasticlogger.data.Bucket;
import com.alisenturk.elasticlogger.data.ElasticData;
import com.alisenturk.elasticlogger.data.FilterType;
import com.alisenturk.elasticlogger.data.OrderByType;
import com.alisenturk.elasticlogger.data.SearchParam;
import com.alisenturk.elasticlogger.data.VariableType;
import com.alisenturk.elasticlogger.service.DataReader;
import com.alisenturk.elasticlogger.service.ElasticService;
import com.alisenturk.elasticlogger.service.ElasticSetting;
import com.alisenturk.elasticlogger.service.HttpResponseData;
import com.alisenturk.elasticlogviewer.entity.ErrorClass;
import com.alisenturk.elasticlogviewer.entity.ErrorSearchCriteria;
import com.alisenturk.elasticlogviewer.enums.Index;
import com.alisenturk.elasticlogviewer.util.Helper;
import com.google.gson.Gson;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 *
 * @author alisenturk
 */
@Stateless
public class ErrorLogDAO implements Serializable{
    
    private ElasticSetting              setting         = null;
    private ElasticService<ErrorClass>  elasticService  = null;
    private void init(){
        setting = new ElasticSetting();
        setting.setHostAddress(Helper.getAppParameterValue("elastic.host")); //host adresi
        setting.setPortNumber(Helper.getAppParameterValue("elastic.port")); //port
        setting.setIndexName(Index.ERRORLOG.getIndexName());
        setting.setMappingName(Index.ERRORLOG.getMappingName());
        
        setting.setDebugMode(true); //Debug modu açar
        
        elasticService = ElasticService.createElasticService(setting);
    }
    
    private ErrorSearchCriteria criteria = new ErrorSearchCriteria();
    
    @PostConstruct
    private void loadInit(){
        init();
    }
    
    public List<String> getProjectList(){
        List<String> list = new ArrayList<>();
        HttpResponseData responseData = null;
        try{
            responseData = elasticService.groupBy("project");
            if(responseData.getStatusCode().equals("OK")){
                Gson        gson        = new Gson();
                ElasticData elasticData = gson.fromJson(responseData.getResponseData(),new ElasticData().getClass());
                List<Bucket> buckets = elasticData.getAggregations().getGroup_by_state().getBuckets();
                if(elasticData!=null){
                    for(Bucket bucket:buckets){
                        list.add(bucket.getKey());
                    }
                }
            }
        }catch(Exception e){
            
        }
        
        return list;
    }
    
    private List<String> getObjectFields(Class clzz){
        List<String> fields = new ArrayList<>();
        Field[] fieldList = clzz.getDeclaredFields();
        for(Field field:fieldList){
            fields.add(field.getName());
        }
        return fields;
    }
    
    public List<ErrorClass> getErrorLogs(){
        List<ErrorClass> list = new ArrayList<>();
        try{
            String[] query = new String[1];
            if(criteria.getKeyword()!=null && criteria.getKeyword().length()>2){
                query[0] = criteria.getKeyword();
            }
            
            String[] project = new String[1];
            project[0] = criteria.getProjectName();
            
            String[] dateRange = new String[2];
            dateRange[0] = Helper.date2String(criteria.getBeginDate(),"yyyyMMddHHmmss");
            dateRange[1] = Helper.date2String(criteria.getEndDate(),"yyyyMMddHHmmss");
            
            List<String> fields = new ArrayList<String>();
            fields.addAll(getObjectFields(ErrorClass.class));
            
            List<SearchParam> searchParams = new ArrayList<>();
            if(query[0]!=null){
                if(criteria.getKeyword()!=null && (criteria.getKeyword().indexOf("*")>-1 || criteria.getKeyword().indexOf("?")>-1) ){
                    searchParams.add(new SearchParam("errorMessage", FilterType.WILDCARD,query, VariableType.STRING));
                }else{
                    searchParams.add(new SearchParam("errorMessage", FilterType.TERM,query, VariableType.STRING));
                }                
            }
            searchParams.add(new SearchParam("project", FilterType.TERM,project, VariableType.STRING));
            searchParams.add(new SearchParam("processDate", FilterType.RANGE,dateRange, VariableType.INT));
            
            HttpResponseData responseData = elasticService.search(searchParams,50, 0, fields, "processDate",OrderByType.DESC);
            if(responseData.getStatusCode().equals("OK")){
                DataReader<ErrorClass> reader = new DataReader<>();
                list = reader.read(responseData.getResponseData(),ErrorClass.class);
            }else{
                System.out.println("HATA..:" + responseData.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ErrorSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(ErrorSearchCriteria criteria) {
        this.criteria = criteria;
    }
    
    
    
}
