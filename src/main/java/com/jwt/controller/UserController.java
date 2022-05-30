package com.jwt.controller;

import com.jwt.domain.request.User;
import com.jwt.response.Response;
import com.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/sign")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        int error = userService.createUser(user);
        System.out.println("error" + error);
        return "";
    }

    @PostMapping("/token/refresh")
    //public String join(HttpServletRequest request, @RequestBody String parameter){
    //public String refreshToken(HttpServletRequest request, @RequestParam("refresh_token") String refreshToken){
    public Response refreshToken(HttpServletRequest request) {
        //System.out.println("1parameter"+refreshToken+"\r\n");
        //System.out.println("2parameter"+request.getParameter("refresh_token")+"\r\n");
        String refreshToken = request.getParameter("refreshToken");
        String accessToken = request.getParameter("accessToken");
        return userService.refreshToken(refreshToken, accessToken);
    }

    @PostMapping("/logout")
    public Response logout(HttpServletRequest request) {
        return userService.logout(request.getParameter("accessToken"));
    }

    @GetMapping("/user")
    public Response user(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION).replace("Bearer ", "");
        return userService.getUserInfo(token);
    }

    @GetMapping("/sign")
    public Response signedUser(HttpServletRequest request) {
        String email=request.getParameter("email");
        return userService.checkSignedEmail(email);
    }
}