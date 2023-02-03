package com.berkan.bachelorprojekt.BA_Bachelorprojekt.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/basicAuth")
    public User user(Principal user) {

        return userService.findByFirstName(user);
    }

    @PutMapping(path = "/adminUpgrade")
    public User adminUpgrade(@RequestBody String email){
        return userService.adminUpgrade(email);
    }

/*    @PostMapping(path = "/basicAuth")
    public User user(@RequestParam String username, @RequestParam String password) {

        return userService.findByFirstName(username);
    }*/

}
