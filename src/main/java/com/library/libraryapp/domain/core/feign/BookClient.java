package com.library.libraryapp.domain.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bookClient", url = "https://fakerapi.it/api/v1/books")
public interface BookClient {

    @GetMapping
    ResponseEntity<FakerApiBookResponse> getBookData(@RequestParam("_quantity") long quantity,
                                                     @RequestParam("_locale") String locale
    );
}
