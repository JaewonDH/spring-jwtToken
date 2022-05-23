package com.jwt.filter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jwt.auth.CustomUserDetail;
import com.jwt.model.User;
import com.jwt.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService=userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JWTProperties.HEADER_STRING);

        if(token==null || !token.startsWith(JWTProperties.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        token=token.replace(JWTProperties.TOKEN_PREFIX,"");

        String email = JWT.require(Algorithm.HMAC512(JWTProperties.SECRET))
                .build()
                .verify(token)
                .getClaim("email")
                .asString();

        if(email!=null){
            User user=userService.getFindByEmail(email);

            CustomUserDetail customUserDetail = new CustomUserDetail(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetail,null,customUserDetail.getAuthorities());
            // 사용자 정보를 가져다가 강제로 세션에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
