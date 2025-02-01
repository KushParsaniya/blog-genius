package dev.kush.bloggenius.repos;

import dev.kush.bloggenius.entities.PublishedBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublishedBlogRepository extends JpaRepository<PublishedBlog, Long> {

    @Query("SELECT pb.title FROM PublishedBlog pb")
    List<String> findAllPublishedBlogTitles();
}