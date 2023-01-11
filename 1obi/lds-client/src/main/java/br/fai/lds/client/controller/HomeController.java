package br.fai.lds.client.controller;

import br.fai.lds.model.entities.UserModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomePage(HttpSession session, Model model) {

        UserModel user = (UserModel) session.getAttribute("currentUser");
        model.addAttribute("user", user);

        return "home";
    }


}
