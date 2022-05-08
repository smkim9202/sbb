package com.mysite.sbb.user;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup") //회원가입을 위한 템플릿을 렌더링
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup") //회원가입을 진행
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        //비밀번호1, 비밀번호2가 동일한지 검증
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            //bindingResult.rejectValue(필드명, 오류코드, 에러메시지)
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        //회원가입시 사용자ID, 이메일주소 중복 확인 후 예외처리
        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    //login PostMapping 방식의 메서드는 스프링 시큐리티가 대신 처리해준다.
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}