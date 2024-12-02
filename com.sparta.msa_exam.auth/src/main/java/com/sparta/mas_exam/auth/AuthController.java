package com.sparta.mas_exam.auth;

import com.sparta.mas_exam.auth.domain.User;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> signIn(@RequestBody AuthRequest signInRequest,
                                                    HttpServletResponse res){
        String token = authService.signIn(signInRequest.getUsername(), signInRequest.getPassword());

        res.addHeader("Authorization", "Bearer " + token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> signUp(@RequestBody AuthRequest signUpRequest) {
        User createdUser = authService.signUp(signUpRequest.getUsername(), signUpRequest.getPassword());
        return ResponseEntity.ok(createdUser);
    }
}
