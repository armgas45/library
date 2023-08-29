package com.library.libraryapp.controller.privacy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicInformationController {

    // We can have the insertion logic of these components implemented in admin controller

    @GetMapping("/api/v1/privacy")
    public ResponseEntity<String> privacyPolicy() {
        return ResponseEntity.status(HttpStatus.OK).body("privacy-policy");
    }

    @GetMapping("/api/v1/terms")
    public ResponseEntity<String> termsAndConditions() {
        return ResponseEntity.status(HttpStatus.OK).body("terms and conditions");
    }
}
