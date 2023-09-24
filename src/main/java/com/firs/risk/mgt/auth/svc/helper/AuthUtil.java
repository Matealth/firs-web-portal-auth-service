package com.firs.risk.mgt.auth.svc.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static Authentication getPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

