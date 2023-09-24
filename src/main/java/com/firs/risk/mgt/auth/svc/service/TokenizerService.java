package com.firs.risk.mgt.auth.svc.service;

import com.firs.risk.mgt.auth.svc.entity.Tokenizer;
import com.firs.risk.mgt.auth.svc.repo.TokenizerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenizerService {

    @Autowired
    private TokenizerRepo tokenizerRepo;

    public Tokenizer save(Tokenizer tokenizer) {
        return tokenizerRepo.save(tokenizer);
    }

    public Tokenizer findByPhoneOrEmail(String phone, String email) {
        return tokenizerRepo.findByPhoneOrEmail(phone, email);
    }

    public Tokenizer findByToken(String token) {
        return tokenizerRepo.findByToken(token);
    }


    public Tokenizer findByPhoneAndToken(String phone, String token) {
        return tokenizerRepo.findByPhoneAndToken(phone, token);
    }

    public Tokenizer findByEmailAndToken(String email, String token) {
        return tokenizerRepo.findByEmailAndToken(email, token);
    }
}