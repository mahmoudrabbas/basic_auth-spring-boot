package com.global.services;

import com.global.entities.Role;
import com.global.entities.User;
import com.global.repositories.RoleRepository;
import com.global.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public List<User> fetchAllUser(){
        return userRepository.findAll();
    }

    public User add(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRoles().isEmpty()){
            Role userRole = roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Role Not Found"));
            user.getRoles().add(userRole);
        }
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("this user is not found"));


        List<GrantedAuthority> authorities = new ArrayList<>()
                ;

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName())));

        return new  org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
