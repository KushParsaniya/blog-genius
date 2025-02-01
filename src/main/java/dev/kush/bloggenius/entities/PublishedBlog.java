package dev.kush.bloggenius.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishedBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long blogId;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String markdownContent;

    @Column(columnDefinition = "TEXT")
    private String htmlContent;

    @Column(columnDefinition = "TEXT")
    private String tags;

    @Column(columnDefinition = "TEXT")
    private String url;

    private Instant createdAt = Instant.now();

    private boolean isOnDevTo = false;

    private boolean isOnDailyDev = false;

    private boolean isOnSlack = false;

    public static PublishedBlog of(long blogId, String title, String description, String markdownContent, String htmlContent, String[] tags, String url, boolean isOnDevTo) {
        return new PublishedBlog(null, blogId, title, description, markdownContent, htmlContent, String.join(",", tags), url, Instant.now(), isOnDevTo, false, false);
    }


}
