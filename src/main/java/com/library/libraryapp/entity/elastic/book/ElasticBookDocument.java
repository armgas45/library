package com.library.libraryapp.entity.elastic.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "books")
@AllArgsConstructor
@NoArgsConstructor
public class ElasticBookDocument {

    @Id
    private Long id;

    @Field(name = "title")
    private String title;

    @Field(name = "author")
    private String author;

}
