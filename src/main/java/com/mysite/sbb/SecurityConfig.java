package com.mysite.sbb;

import com.mysite.sbb.user.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration //스프링의 환경설정 파일임을 의미하는 애너테이션
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션 : 내부적으로 SpringSecurityFilterChain이 동작하여 URL필터가 적용
@EnableGlobalMethodSecurity(prePostEnabled = true)//prePostEnabled=true 로그인 여부를 판별하기 위해 사용했던 @PreAuthorize 애너테이션 사용하기 위해 필요
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSecurityService userSecurityService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //모든 인증되지 않은 요청을 허락 : 로그인 없이도 모든 페이지 접근 가능
        http.authorizeHttpRequests().antMatchers("/**").permitAll()
                //http 객체의 설정을 이어서 할 수 있게 하는 메서드이다.
                .and()
                    // /h2-console/로 시작하는 URL은 CSRF 검증을 하지 않는다는 설정
                    .csrf().ignoringAntMatchers("/h2-console/**")
                //X-Frame-Options 헤더값을 sameorigin으로 설정하여 오류가 발생하지 않도록 설정
                //frame에 포함된 페이지가 페이지를 제공하는 사이트와 동일한 경우에 계속 사용할 수 있다.
                .and()
                    .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))

                .and()
                    //스프링 시큐리티의 로그인 설정을 담당하는 부분 : 로그인 페이지의 URL은 '/user/login'이고 성공시에 디폴트 페이지는 루트URL(/)임
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                .and()
                    //스프링 시큐리티의 로그아웃 설정을 담당하는 부분 : 로그아웃 페이지의 URL은 '/user/logout'이고 성공시 디폴트 페이지는 루트URL(/)임
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    //로그아웃시 생성된 사용자 세션도 삭제하도록 처리
                    .invalidateHttpSession(true)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //AuthenticationManagerBuilder : 스프링 시큐리티의 인증을 담당

        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
        //auth 객체에 위에서 작성한 UserSecurityService를 등록하여 사용자 조회를 UserSecurityService가 담당하도록 설정
        //이때 비밀번호 검증에 사용할 passwordEncoder도 등록
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
