package com.berkan.bachelorprojekt.BA_Bachelorprojekt.Registration;

import com.berkan.bachelorprojekt.BA_Bachelorprojekt.User.User;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PutMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody User user){

        registrationService.register(user);

        return new ResponseEntity<String>("Successfully created Account", HttpStatus.CREATED);
    }
}
