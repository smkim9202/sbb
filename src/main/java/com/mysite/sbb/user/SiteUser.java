package com.mysite.sbb.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SiteUser { //스프링시큐리티에 User 클래스와 헷갈리지 않기 위해 SiteUser로 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //unique=true : 유일한 값만 저장 가능(중복값 저장 불가)
    private String username; //사용자 이름(사용자 ID)

    private String password; //비밀번호

    @Column(unique = true)
    private String email; //이메일
}
