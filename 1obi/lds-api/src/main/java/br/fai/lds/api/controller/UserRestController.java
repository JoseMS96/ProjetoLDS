package br.fai.lds.api.controller;

import br.fai.lds.api.service.UserRestService;
import br.fai.lds.model.entities.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    private UserRestService<UserModel> userService;

    @GetMapping("/find")
    public ResponseEntity<List<UserModel>> find() {

        List<UserModel> users = userService.find();

        if (users == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(users);
    }

    private String lalala() {
        return "asdasdasd";
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserModel> findById(@PathVariable("id") final int id) {

        UserModel user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") final int id) {

        boolean result = userService.deleteById(id);

        return result ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody final UserModel user) {

        int userId = userService.create(user);

//        return userId == -1 ? ResponseEntity.badRequest().build()
//                : ResponseEntity.ok(userId);

        if (userId == -1) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable("id") final int id, @RequestBody final UserModel entity) {

        boolean response = userService.update(id, entity);

        return response ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().build();
    }


}
