package com.sparta.mas_exam.auth;

import com.sparta.mas_exam.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody RequestDto signInRequest){
        String token = authService.signIn(signInRequest.getUsername(), signInRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> signUp(@RequestBody RequestDto signUpRequest) {
        User createdUser = authService.signUp(signUpRequest.getUsername(), signUpRequest.getPassword());
        return ResponseEntity.ok(createdUser);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RequestDto {
        private String username;
        private String password;
    }
}
