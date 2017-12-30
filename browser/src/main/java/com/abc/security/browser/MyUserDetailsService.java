package com.abc.security.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义用户登录信息获取的逻辑
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("登录用户名：{}",username);
        // 将密码模拟成加密的，
        // 实际上应该在注册时用PasswordEncoder加密密码，存入数据库，
        // 然后从数据库读出来就是已加密的密码
        String encodedPassword = passwordEncoder.encode("123456");
        logger.info("用户 {} 的密码是： {}",username,encodedPassword);
        //模拟从数据库根据用户名读取用户信息
        return new User(username,encodedPassword,
                true,true,true,true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}
