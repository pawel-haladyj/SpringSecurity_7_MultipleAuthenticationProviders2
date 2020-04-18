package pl.haladyj.springsecurity7.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.haladyj.springsecurity7.security.authentication.UsernamePasswordAuthentication;
import pl.haladyj.springsecurity7.service.JpaUserDetailsService;

import java.util.List;

@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider{

    @Autowired
    private JpaUserDetailsService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = service.loadUserByUsername(username);

        if(passwordEncoder.matches(password,user.getPassword())){
            return  new UsernamePasswordAuthentication(username,password, List.of(()-> "read"));
        }
        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthentication.class.equals(aClass);


    }
}
