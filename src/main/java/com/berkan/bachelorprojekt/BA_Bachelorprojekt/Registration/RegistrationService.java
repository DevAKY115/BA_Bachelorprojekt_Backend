package com.berkan.bachelorprojekt.BA_Bachelorprojekt.Registration;

import com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions.UserException;
import com.berkan.bachelorprojekt.BA_Bachelorprojekt.User.User;
import com.berkan.bachelorprojekt.BA_Bachelorprojekt.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder encoder;

    public User register(User user){


        System.out.println(repository.findByEmail(user.getEmail()).isPresent());

        if(repository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException("User with this e-mail already exists");
        }


        user.setRoles("ROLE_USER");

        user.setPassword(encoder.encode(user.getPassword()));

        return repository.save(user);
    }
}
