package com.kpbs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ViewController {

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", "hohoho");
        return "hello";
    }
}
