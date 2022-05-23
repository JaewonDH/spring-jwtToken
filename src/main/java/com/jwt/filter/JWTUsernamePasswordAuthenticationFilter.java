package com.jwt.filter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.auth.CustomUserDetail;
import com.jwt.model.User;
import com.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 시큐리티 스프링을 동작 위해서 만든 SecurityConfig의 configure(HttpSecurity http) 콜백 함수에 http로 .add filter 등록하기 위한 필터
@RequiredArgsConstructor
public class JWTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("로그인 성공 시 호출 되는 콜백 successfulAuthentication");

        // authResult에 UserDetails의 정보를 가지고 있다.
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();

        // JWT 토근 생성
        String jwtToken = JWT.create()
                .withSubject("testToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                .withClaim("id", customUserDetail.getId())
                .withClaim("email", customUserDetail.getUsername())
                .sign(Algorithm.HMAC512(JWTProperties.SECRET)); // 암호키

        // 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + jwtToken);

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
