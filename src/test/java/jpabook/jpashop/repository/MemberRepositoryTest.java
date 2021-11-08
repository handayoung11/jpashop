package jpabook.jpashop.repository;

import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MemberRepositoryTest {

//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Transactional
//    public void test() throws Exception {
//        //given
//        Member member = new Member();
//        member.setUsername("memberA");
//
//        //when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(1L);
//
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember).isEqualTo(member);
//    }
}