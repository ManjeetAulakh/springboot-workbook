package com.pep.springjpa.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pep.springjpa.models.User;
import com.pep.springjpa.repo.UserRepo;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    // Inject UserRepository through constructor
    public CustomUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
System.out.println("Resolved roles for user: " + user.getRoles());

List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
    .map(SimpleGrantedAuthority::new)
    .collect(Collectors.toList());

System.out.println("Authorities: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role)) 
                        .collect(Collectors.toList())
        );
        
    }
}