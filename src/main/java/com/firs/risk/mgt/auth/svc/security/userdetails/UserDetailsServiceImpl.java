package com.firs.risk.mgt.auth.svc.security.userdetails;

import com.firs.risk.mgt.auth.svc.entity.User;
import com.firs.risk.mgt.auth.svc.exception.UnauthorizedException;
import com.firs.risk.mgt.auth.svc.repo.RoleRepo;
import com.firs.risk.mgt.auth.svc.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    private Locale locale = new Locale("en", "NG");
    private ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    public User save(User user) {
        return userRepo.save(user);
    }

    public User findByEmail(String email){
        return userRepo.findByUsername(email);
    }


    public User findByPhone(String phone){
        return userRepo.findByPhone(phone);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found n the database {}", username);
            throw new UsernameNotFoundException(messages.getString("user-not-found"));
        } else {
            log.info(" User found in the database {}", user.getEnabled());
            if(!user.getEnabled())
                throw new UnauthorizedException(messages.getString("account-disabled"));
        }
        return UserDetailsImpl.build(user);
    }

    public void setLocale(Locale locale){
        this.locale = locale;
        messages = ResourceBundle.getBundle("messages", this.locale);
    }
}
