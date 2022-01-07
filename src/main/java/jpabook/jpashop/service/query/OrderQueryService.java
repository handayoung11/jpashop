package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderRepository orderRepository;

    public List<Order> findAllWithLazyLoadMemAndAddAndOrI() {
        List<Order> orders = orderRepository.findAllByOrderSearch(new OrderSearch());
        for (Order o : orders) {
            o.getMember().getName();
            o.getDelivery().getAddress();
            o.getOrderItems().forEach(i -> i.getItem().getName());
        }
        return orders;
    }
}
