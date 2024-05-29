package com.site.springboot.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "tb_news_comment")
public class NewsComment {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "comment_id")
    private Long commentId;

    @Setter
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "commentator")
    private String commentator;

    @Column(name = "comment_body")
    private String commentBody;

    @Setter
    @Column(name = "comment_status")
    private Byte commentStatus;

    @Setter
    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Setter
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreationTimestamp
    private Date createTime;

    public void setCommentator(String commentator) {
        this.commentator = commentator == null ? null : commentator.trim();
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody == null ? null : commentBody.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", commentId=").append(commentId);
        sb.append(", newsId=").append(newsId);
        sb.append(", commentator=").append(commentator);
        sb.append(", commentBody=").append(commentBody);
        sb.append(", commentStatus=").append(commentStatus);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append("]");
        return sb.toString();
    }
}