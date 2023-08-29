package com.library.libraryapp.repository.book;

import com.library.libraryapp.entity.elastic.book.ElasticBookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticBookRepository extends ElasticsearchRepository<ElasticBookDocument, Long> {
}
