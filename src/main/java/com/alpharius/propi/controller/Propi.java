package com.alpharius.propi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Propi {

    @GetMapping("/hellothere")
    public String hellothere() {
        return "General Kenobi";
    }

}
