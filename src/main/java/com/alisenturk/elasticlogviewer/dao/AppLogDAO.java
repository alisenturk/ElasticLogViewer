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
import com.alisenturk.elasticlogviewer.entity.AppLogData;
import com.alisenturk.elasticlogviewer.entity.AppLogSearchCriteria;
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
public class AppLogDAO implements Serializable{
    
    private ElasticSetting              setting         = null;
    private ElasticService<AppLogData>  elasticService  = null;
    private void init(){
        setting = new ElasticSetting();
        setting.setHostAddress(Helper.getAppParameterValue("elastic.host")); //host adresi
        setting.setPortNumber(Helper.getAppParameterValue("elastic.port")); //port
        setting.setIndexName(Index.APPLOG.getIndexName());
        setting.setMappingName(Index.APPLOG.getMappingName());
        
        setting.setDebugMode(true); //Debug modu açar
        
        elasticService = ElasticService.createElasticService(setting);
    }
    
    private AppLogSearchCriteria criteria = new AppLogSearchCriteria();
    
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
    
    public List<String> getServiceNametList(){
        List<String> list = new ArrayList<>();
        HttpResponseData responseData = null;
        try{
            responseData = elasticService.groupBy("serviceName");
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
    
    public List<String> getTransactiontList(){
        List<String> list = new ArrayList<>();
        HttpResponseData responseData = null;
        try{
            responseData = elasticService.groupBy("transactionId");
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
    
    public List<AppLogData> getAppLogs(){
        List<AppLogData> list = new ArrayList<>();
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
            fields.addAll(getObjectFields(AppLogData.class));
            
            List<SearchParam> mustParams    = new ArrayList<>();
            List<SearchParam> shouldParams  = new ArrayList<>();
            mustParams.add(new SearchParam("project", FilterType.TERM,project, VariableType.STRING));
            mustParams.add(new SearchParam("processDate", FilterType.RANGE,dateRange, VariableType.INT));
            
            if(criteria.getServiceName()!=null && criteria.getServiceName().length()>2){
                String[] service = new String[1];
                service[0] = criteria.getServiceName();
                mustParams.add(new SearchParam("serviceName", FilterType.TERM,service, VariableType.STRING));
            }
            if(criteria.getTransactionId()!=null && criteria.getTransactionId().length()>2){
                String[] service = new String[1];
                service[0] = criteria.getTransactionId();
                mustParams.add(new SearchParam("transactionId", FilterType.TERM,service, VariableType.STRING));
            }
            
            if(query[0]!=null){
                
                if(criteria.getKeyword()!=null && (criteria.getKeyword().indexOf("*")>-1 || criteria.getKeyword().indexOf("?")>-1) ){
                    String words[] = criteria.getKeyword().split(" ");
                    
                    String str2[] = new String[1];
                    
                    for(String word:words){
                        str2 = new String[1];
                        str2[0] = word;                        
                        shouldParams.add(new SearchParam("requestData", FilterType.WILDCARD,str2, VariableType.STRING));                        
                    
                    }
                    
                    
                    for(String word:words){
                        str2 = new String[1];
                        str2[0] = word;                        
                        shouldParams.add(new SearchParam("responseData", FilterType.WILDCARD,str2, VariableType.STRING));
                    
                    }                                       
                    
                }else if(criteria.getKeyword()!=null && criteria.getKeyword().indexOf(" ")>-1 && (criteria.getKeyword().indexOf("*")>-1 || criteria.getKeyword().indexOf("?")>-1) ){
                    shouldParams.add(new SearchParam("requestData", FilterType.REGEXP,query, VariableType.STRING));
                    shouldParams.add(new SearchParam("responseData", FilterType.REGEXP,query, VariableType.STRING));
                }else{
                    shouldParams.add(new SearchParam("requestData", FilterType.TERM,query, VariableType.STRING));
                    shouldParams.add(new SearchParam("responseData", FilterType.TERM,query, VariableType.STRING));
                }                
            }
            
            HttpResponseData responseData = elasticService.search(mustParams,shouldParams,250, 0, fields, "processDate",OrderByType.DESC);
            if(responseData.getStatusCode().equals("OK")){
                DataReader<AppLogData> reader = new DataReader<>();
                list = reader.read(responseData.getResponseData(),AppLogData.class);
            }else{
                System.out.println("HATA..:" + responseData.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public AppLogSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(AppLogSearchCriteria criteria) {
        this.criteria = criteria;
    }

    
    
}
