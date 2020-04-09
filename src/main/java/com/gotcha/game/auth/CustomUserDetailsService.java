package com.gotcha.game.auth;

import com.gotcha.game.exceptions.NoSuchUserException;
import com.gotcha.game.model.User;
import com.gotcha.game.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findByEmail(email);

        if(user.isEmpty())
            throw new NoSuchUserException("No such user found with" + email);
        return new CustomUserDetails(user.get());
    }
}
