package com.propets.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "posts")
public class PostEntity {
    @Id
    private long id;
    private String ownerId;//user email how posted
    private LocalDateTime postDate;
    private String text;
    private List<String>images;
    private boolean complaint;
}
