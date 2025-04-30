package com.fadhiilabiyyi.license3j.RestController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/testing")
    public ResponseEntity<?> testing() {
        try {
            return new ResponseEntity<>("Test", HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error("", e);
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }
}
