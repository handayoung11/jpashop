package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        int stockQuantity = 10;
        Book book = createBook(stockQuantity);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals(stockQuantity - orderCount, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();

        int stockQuantity = 10;
        Book book = createBook(stockQuantity);

        //when
        assertThrows(NotEnoughStockException.class
                , () -> orderService.order(member.getId(), book.getId(), stockQuantity + 1)
                , "아이템의 재고보다 많은 수량을 주문하면 에러가 발생해야 한다.");

        //then
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        int stockQuantity = 10;
        Book book = createBook(stockQuantity);
        Long orderId = orderService.order(member.getId(), book.getId(), 2);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소 시 상태는 CANCEL이다.");
        assertEquals(stockQuantity, book.getStockQuantity(), "주문이 취소된 상품은 재고가 복구되어야 한다.");
    }

    private Book createBook(int stockQuantity) {
        Book book = new Book();
        book.setName("어린왕자");
        book.setPrice(10000);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "123-123"));
        em.persist(member);
        return member;
    }
}