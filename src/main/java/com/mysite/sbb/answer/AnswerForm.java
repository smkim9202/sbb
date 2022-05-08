package com.mysite.sbb.answer;

import com.mysite.sbb.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class AnswerForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    @ManyToMany
    //하나의 질문에 여러사람이 추처천 할 수 있고, 여러 개의 질문을 추천 가능
    //대등한 관계
    Set<SiteUser> voter; //추천인
    //Set :중복을 허용하지 않는 자료형
}
