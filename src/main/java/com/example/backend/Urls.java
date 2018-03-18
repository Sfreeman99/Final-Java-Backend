package com.example.backend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class Urls {
    @PostMapping("/signup")
    public HashMap signup(@RequestBody User newUser){
        SignUpUserValidation userValidation = new SignUpUserValidation(newUser);
        if (userValidation.isValid())
            return null;
        else
            return userValidation.validationErrors();
    }
}
