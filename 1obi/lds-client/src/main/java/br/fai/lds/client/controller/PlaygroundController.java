package br.fai.lds.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaygroundController {

    @GetMapping("/playground/test")
    public String getTestPage() {
        return "playground/test-page";
    }

}
