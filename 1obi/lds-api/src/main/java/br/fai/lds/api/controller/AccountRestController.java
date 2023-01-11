package br.fai.lds.api.controller;

import br.fai.lds.api.service.UserRestService;
import br.fai.lds.model.entities.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountRestController {

    @Autowired
    private UserRestService userRestService;

    @PostMapping("/login")
    public ResponseEntity<UserModel> login(
            @RequestHeader("Authorization") String encodedData) {

        System.out.println("Request com base64: " + encodedData);

        UserModel userModel = userRestService.validateLogin(encodedData);

        if (userModel == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userModel);
    }


}
