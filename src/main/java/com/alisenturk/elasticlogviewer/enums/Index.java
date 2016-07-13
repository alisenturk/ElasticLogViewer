package com.alisenturk.elasticlogviewer.enums;

/**
 *
 * @author alisenturk
 */
public enum Index {

    ERRORLOG("apperrordb","errorlog","Uygulamalardaki hataların durduğu tablo"),
    APPLOG("applogdb","applog","Uygulama logları");

    Index(String indexName, String mappingName, String description) {
        this.indexName = indexName;
        this.mappingName = mappingName;
        this.description = description;
    }
    
    private String  indexName;
    private String  mappingName;
    private String  description;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
