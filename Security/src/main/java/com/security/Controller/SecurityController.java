package com.security.Controller;

import com.security.Constants.SecurityConstants;
import com.security.DTO.EmailDTO;
import com.security.DTO.LoginDTO;
import com.security.Entity.Role;
import com.security.Entity.User;
import com.security.Repository.RoleRepository;
import com.security.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder()
                    .claim("UUID", this.userRepository.findByEmail(authentication.getName()).get().getUuid())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 10 * 60 * 10000))
                    .signWith(key).compact();

            ResponseCookie jwtCookie = ResponseCookie.from(SecurityConstants.JWT_HEADER, jwt).path("/").maxAge(10 * 60 * 10000).build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody LoginDTO loginDTO){
        if (!this.userRepository.findByEmail(loginDTO.getEmail()).isEmpty())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");

        Set<Role> role = new HashSet<>();
        role.add(this.roleRepository.findByRole("ROLE_INDIVIDUAL"));
        User user = User.builder().email(loginDTO.getEmail()).password(passwordEncoder.encode(loginDTO.getPassword())).roles(role).build();

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
