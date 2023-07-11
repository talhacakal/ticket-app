package com.security.Controller;

import com.netflix.discovery.DiscoveryClient;
import com.security.Constants.SecurityConstants;
import com.security.DTO.EmailDTO;
import com.security.DTO.LoginDTO;
import com.security.Entity.User;
import com.security.Repository.RoleRepository;
import com.security.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String greeting(){
        return "hello";
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .claim("username", authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 10 * 60 * 10000))
                .signWith(key).compact();

        ResponseCookie jwtCookie = ResponseCookie.from(SecurityConstants.JWT_HEADER, jwt).path("/").maxAge(10 * 60 * 10000).build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody LoginDTO loginDTO){
        if (!this.userRepository.findByEmail(loginDTO.getEmail()).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists.");
        }

        User user = User.builder()
                .email(loginDTO.getEmail())
                .password(passwordEncoder.encode(loginDTO.getPassword()))
                .role(this.roleRepository.findById(1).get())
                .build();

        User createdUser = this.userRepository.save(user);
        EmailDTO emailDTO = EmailDTO.builder()
                .to(createdUser.getEmail())
                .subject("Registration")
                .body("Success").build();

        WebClient webClient = WebClient.builder().build();
        webClient.post()
                .uri("http://localhost:8050/api/email")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(emailDTO))
                .exchange().subscribe();


        return ResponseEntity.ok().body("");
    }

    @GetMapping("/validate")
    public ResponseEntity validate(HttpServletRequest request){

        return ResponseEntity.ok("OK");
    }

}
