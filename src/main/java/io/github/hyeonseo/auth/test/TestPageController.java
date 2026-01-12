package io.github.hyeonseo.auth.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestPageController {

    @GetMapping
    public String index() {
        return "test/index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "test/signup";
    }

    @GetMapping("/success")
    public String success() {
        return "test/success";
    }

}
