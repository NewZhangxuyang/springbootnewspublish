package com.site.springboot.core.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsVO {

    private Long newsId;

    private String newsTitle;

    private Long newsCategoryId;

    private String newsCoverImage;

    private String newsContent;

    private Byte newsStatus;

    private Long newsViews;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private int  praiseCount;
}
