package com.site.springboot.core.util;

import com.alibaba.excel.EasyExcel;
import com.site.springboot.core.entity.News;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelUtil {
    public void newsExport(List<News> list, HttpServletResponse response) {
        try {
            String fileName = new String("news.xlsx".getBytes(), StandardCharsets.ISO_8859_1);
            response.setContentType("application/msexcel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            EasyExcel.write(response.getOutputStream(), News.class)
                    .sheet("sheet")
                    .doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
