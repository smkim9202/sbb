package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.zip.DataFormatException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        //암호화를 위해 시큐리티의 BCryptPasswordEncoder 클래스를 사용하여 암호하 후 비밀번호 저장
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //passwordEncoder 객체를 직접 new로 생성하면 암호화 방식 변경시 사용한 모든 프로그램을 일일히 찾아서 수정해야 함
        //=>SecurityConfig에 @Bean으로 등록해서 사용해서 해결
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    //로그인한 사용자명을 통해 SiteUser 객체를 조회
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
