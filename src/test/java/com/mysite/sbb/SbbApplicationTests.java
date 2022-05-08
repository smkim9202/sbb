package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import com.mysite.sbb.Question.Question;
import com.mysite.sbb.Question.QuestionRepository;

import com.mysite.sbb.answer.Answer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	//@Transactional 애너테이션 없이 실행한다면?

	//org.hibernate.LazyInitializationException 오류 발생
	//findById() 호출하여 Question 객체 조회 후 DB세션 끊어짐 => DB세션이 종료된 후 실행하는 q.getAnswerList();에서오류 발생
	//답변데이터 리스트는 q 객체를 조회 할 때 가져오지 않고, q.getAnswerList() 메서드를 호출하는 시점에서 가져오기 때문이다.

	//필요한 시점에 데이터를 가져오는 방식 : Lazy 방식 / q 객체 조회 시 답변 리스트를 모두 가져오는 방식 : Eager 방식
	//@OneToMany, @ManyToOne 애너테이션의 옵션으로 fetch=FetchType.LAZY 또는 fetch=FetchType.EAGER 처럼 지정 가능

	//이 문제는 테스트 코드에서만 발생하는데, 실제 서버에서는 JPA 프로그램 실행 시 DB세션이 종료되지 않기 때문에 LazyInitializationException이 발생하지 않는다.
	//테스트 코드 수행 시 오류 방지하는 간단한 방법 : @Transactional 애너테이션 사용 - 메서드 종료 될 때까지 DB세션 유지

	@Transactional //메서드 종료될 때까지 DB세션을 유지하는 애너테이션
	@Test
	void testJpa() {
		//given : Question Id의 값이 2인 데이터가 주어지고
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent()); //Id값의 2인 데이터가 null이 아니라면
		Question q = oq.get();

		//when : getAnswerList() 답변리스트를 가져 왔을 때
		List<Answer> answerList = q.getAnswerList();

		//then
		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());

	}
}
