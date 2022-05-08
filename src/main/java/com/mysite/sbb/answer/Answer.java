package com.mysite.sbb.answer;

import com.mysite.sbb.Question.Question;
import com.mysite.sbb.user.SiteUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
        //N:1 관계에 적용하는 애너테이션으로 어떤엔티티와 연결된 속성이라는 것을 명시적으로 표시
        //실제 DB에서 foreignKey 관계가 생성
        //많은것(답변)은 Many, 하나(질문)는 One
        //부모는 Question 자식은 Answer로 부모 자식관계를 갖는 구조
    private Question question;
        //질문 엔티티 참조하기 위해 추가 : 어떤 질문의 답변인지 알아야 함
        //답변 객체를 통해 질문 객체의 제목 알고 싶은 경우 : answer.getQuestion().getSubject()처럼 접근



    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate; //수정 일시

    @ManyToMany
    //하나의 질문에 여러사람이 추처천 할 수 있고, 여러 개의 질문을 추천 가능
    //대등한 관계
    Set<SiteUser> voter; //추천인
    //Set :중복을 허용하지 않는 자료형
}