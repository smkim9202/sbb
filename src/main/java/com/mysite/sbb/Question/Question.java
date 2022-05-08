package com.mysite.sbb.Question;


import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter //롬복 애너테이션으로 Getter 메서드 자동으로 생성
@Setter //롬복 애너테이션으로 Setter 메서드 자동으로 생성
@Entity //JPA가 엔티티로 인식하게 해주는 애너테이션
        //컬럼의 세부 설정을 하고 싶을 경우 @Column(속성명 = 속성값) 애너테이션 사용
        //@Column 애너테이션 생략시에도 컬럼으로 인식
        //컬럼으로 인식하고 싶지 않을 경우 @Transient 애너테이션 사용
public class Question {

    @Id //기본 키로 지정하는 애너테이션
    //데이터를 구분하는 유효한 값인 고유번호 속성에 적용해서 중복되지 않게 함
    //DB에서 기본키(primary key)를 엔티티에서는 @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //1씩 자동으로 증가하여 저장하는 애너테이션
    //strategy : 고유번호 생성하는 옵션. 옵션 생략 할 경우 @GeneratedValue으로 지정된 컬럼들이 모두 동일한 시퀀스로 번호를 생성 됨
    //strategy = GenerationType.IDENTITY : 해당 컬럼만의 독립적인 시퀀스 생성하여 번호를 증가시킬 때 사용
    private Integer id;

    @Column(length = 200)
    //length : 컬럼의 길이 설정
    private String subject;

    @Column(columnDefinition = "TEXT")
    //columnDefinition : 컬럼의 속성을 정의
    //columnDefinition = "TEXT" : 글자수를 제한 할 수 없는 경우 사용
    private String content;

    private LocalDateTime createDate;
    //엔티티에 작성한 컬럼명이 createDate 라면 실제 DB 테이블의 컬럼명은 create_date가 된다.
    //카멜케이스이름은 모두 소문자로 변경 후 언더바(_)가 단어를 구분한다.


    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    //1:N 관계에 적용하는 애너테이션
    //실제 DB에서 ForeignKey 관계가 생성
    //mappedBy : 참조 엔티티 속성명으로 여기서는 답변엔티티에서 질문엔티티를 참조한 속성명을 전달해야한다.
    //cascade = CascadeType.REMOVE : 질문을 삭제하면 그에 달린 답변들도 모두 삭제하기 위한 속성=속성값
    private List<Answer> answerList;
    //질문 하나에 답변은 여러개이므로 답변 속성은 List 형태로 구성
    //질문 객체에서 답변 참조 : question.getAnswerList() 호출


    @ManyToOne
    //author 속성은 StieUser 엔티티의 @ManyToOne으로 적용
    //여러개 질문의 한 명의 사용자에게 작성 될 수 있으므로
    private SiteUser author;

    private LocalDateTime modifyDate; //수정 일시

    @ManyToMany
            //하나의 질문에 여러사람이 추처천 할 수 있고, 여러 개의 질문을 추천 가능
            //대등한 관계
    Set<SiteUser> voter; //추천인
        //Set :중복을 허용하지 않는 자료형

}
