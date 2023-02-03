package com.berkan.bachelorprojekt.BA_Bachelorprojekt.User;

import com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    public User findByFirstName(Principal user){

        User result = repository.findByEmail(user.getName()).get();
        result.setPassword("");

        return result;
    }

    public User findByEmail(String email){
        System.out.println(email);

        User result = repository.findByEmail(email).get();
        result.setPassword("");

        return result;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        Optional<User> user = repository.findByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException(""));

        System.out.println(new MyUserDetails(user.get()));

        return new MyUserDetails(user.get());


//        return user.map(MyUserDetails::new).get();
    }


    public User adminUpgrade(String email){
        if(repository.findByEmail(email).isPresent()){
            User user = repository.findByEmail(email).get();

            user.setRoles("ROLE_USER,ROLE_ADMIN");

            System.out.println("Upgraded user \"" + user.getEmail() + "\" has been upgraded to admin");

            repository.save(user);

            return user;
        }

        throw new UserException("No User found with this email: " + email);
    }
}
