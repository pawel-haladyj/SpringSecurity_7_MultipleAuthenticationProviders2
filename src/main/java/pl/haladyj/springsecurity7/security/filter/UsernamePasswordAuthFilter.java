package pl.haladyj.springsecurity7.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.haladyj.springsecurity7.entity.Otp;
import pl.haladyj.springsecurity7.repository.OtpRepository;
import pl.haladyj.springsecurity7.security.authentication.OtpAuthentication;
import pl.haladyj.springsecurity7.security.authentication.UsernamePasswordAuthentication;
import pl.haladyj.springsecurity7.security.managers.TokenManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;


public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        var username = request.getHeader("username");
        var password = request.getHeader("password");
        var otp = request.getHeader("otp");

        if(otp==null){
            Authentication authentication = new UsernamePasswordAuthentication(username,password);
            authentication = manager.authenticate(authentication);

            String code = String.valueOf(new Random().nextInt(8999)+1000);

            Otp otpEntity = new Otp();
            otpEntity.setUsername(username);
            otpEntity.setOtp(code);
            otpRepository.save(otpEntity);

        } else {
            Authentication authentication = new OtpAuthentication(username,otp);
            authentication = manager.authenticate(authentication);

            String token = UUID.randomUUID().toString();
            tokenManager.add(token);
            response.setHeader("Authorization", "Authorization: " + token);

        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }
}
