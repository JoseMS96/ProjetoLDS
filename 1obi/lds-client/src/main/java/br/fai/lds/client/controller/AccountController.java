package br.fai.lds.client.controller;

import br.fai.lds.client.service.UserService;
import br.fai.lds.model.entities.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    UserService userService;

    @GetMapping("/sign-up")
    public String getSignUpPage() {
        return "account/sign-up";
    }

    @GetMapping("/sign-in")
    public String getSignInPage() {
        return "account/sign-in-page";
    }

    @GetMapping("/password-recovery")
    public String getPasswordRecoveryPage() {
        return "account/password-recovery";
    }

    @PostMapping("/create")
    public String create(UserModel user) {

        userService.create(user);

        return "redirect:/account/sign-in";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam("username") final String username,
//                        @RequestParam("password") final String password) {
//
//        UserModel userModel = userService.validateUsernameAndPassword(username,
//                password);
//        if (userModel == null) {
//            return "redirect:/account/sign-in";
//        }
//
//        return "redirect:/";
//    }

    @GetMapping("/profile")
    public String getProfilePage(final Model model,
                                 final HttpSession session) {

        UserModel user = (UserModel) session.getAttribute("currentUser");

        if (user == null) {
            // futuramente iremos injetar o erro
            return "redirect:/user/list";
        }

        model.addAttribute("user", user);

        return "user/detail";
    }

}
