# spring-jwtToken

# spring-security

1. WebSecurityConfigurerAdapter 상속 받아 SecurityConfig 만든다.(/src/main/java/config/)
2. configure(HttpSecurity http) Override 하여 http에 각 옵션 security 설정 진행
3. UsernamePasswordAuthenticationFilter 상속받아 JwtAuthFilter 만들어 SecurityConfig configure 함수의 http에 addfilter로 등록 한다. 
4. http://localhost:8090/login에 body 값 전달 시 JwtAuthFilter에 attemptAuthentication 함수가 호출 되어 사용자 정보를 직접 UsernamePasswordAuthenticationToken 통해 토큰을 생성하여 Authentication 설정 하여 리턴 UserDetailService의 loadUserByUsername 호출 되어 여기서 db의에서 현재 사용자가 맞는지 확인 후 UserDetails 리턴하면 로그인이 완료된 상태이다
body {
   "email": "aabbaa@gmail.com", 
   "password": "abcdefg"
  }
5. 로그인이 완료 되면 JwtAuthFilter successfulAuthentication 호출 되어 여기서 jwt 토큰을 만들어 헤더에 Authorization 키와 value값으로 토큰을 추가 하면 클라이언트에 헤더 값을 전달 받을 수 있다.

