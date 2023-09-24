package com.firs.risk.mgt.auth.svc.repo;

import com.firs.risk.mgt.auth.svc.entity.Tokenizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenizerRepo extends JpaRepository<Tokenizer, Long> {
    Tokenizer findByToken(String token);
    Tokenizer findByPhoneOrEmail(String phone, String email);
    Tokenizer findByPhoneAndToken(String phone, String token);
    Tokenizer findByEmailAndToken(String email, String token);
}
