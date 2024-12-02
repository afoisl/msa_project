package com.sparta.mas_exam.auth;

import com.sparta.mas_exam.auth.core.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
