package com.courseselection.professorservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfessorController {

    @PostMapping("/api/signup")
    public String signup() {
        return "hello world";
    }

}
