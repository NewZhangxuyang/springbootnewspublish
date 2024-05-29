package com.site.springboot.core.entity;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.io.Serial;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "detail_search", createIndex = true)
@AllArgsConstructor
@NoArgsConstructor
public class NewsIndex implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Field(type = FieldType.Long)
    private Long newsId;

    @Field(type = FieldType.Keyword)
    private String newsTitle;

    @Field(type = FieldType.Long)
    private Long newsCategoryId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String newsContent;

    @Field(type = FieldType.Keyword)
    private String newsCoverImage;

    @Field(type = FieldType.Byte)
    private Byte newsStatus;

    @Field(type = FieldType.Long)
    private Long newsViews;

    @Field(type = FieldType.Byte)
    private Byte isDeleted;


    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private Date createTime;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private Date updateTime;

}
