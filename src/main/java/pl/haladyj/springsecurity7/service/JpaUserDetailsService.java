package pl.haladyj.springsecurity7.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.haladyj.springsecurity7.entity.User;
import pl.haladyj.springsecurity7.repository.UserRepository;
import pl.haladyj.springsecurity7.security.model.SecurityUser;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository
                .findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        SecurityUser securityUser = new SecurityUser(user);
        return securityUser;
    }
}
