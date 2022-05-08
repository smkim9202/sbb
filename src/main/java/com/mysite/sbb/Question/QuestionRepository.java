package com.mysite.sbb.Question;

import com.mysite.sbb.Question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    //JpaRepository 인터페이스를 상속 받으면 리포지터리가 된다.
    //JpaRepository 상속 시 제네릭스 타입으로 <엔티티타입, 해당 엔티티의 PK 속성의 타입>지정

    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);

    //Page<Question> findAll(Specification<Question> spec, Pageable pageable); //검색
    //@Query 애너테이션이 적용된 findAllByKeyword 메서드를 추가 : 쿼리를 직접 구현
    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
