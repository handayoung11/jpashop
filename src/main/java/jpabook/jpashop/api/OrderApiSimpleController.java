package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiSimpleController {
    private final OrderRepository orderRepository;

    /**
     * ManyToOne, OneToOne
     * Order -> Member
     * Order -> Delivery
     */
    @GetMapping("/api/v2/simple-orders")
    public SimpleOrdersResult ordersV2() {
        List<SimpleOrderDTO> all =
                orderRepository.findAll(new OrderSearch())
                        .stream().map(SimpleOrderDTO::new)
                        .collect(Collectors.toList());
        return new SimpleOrdersResult(all);
    }

    @Data
    @AllArgsConstructor
    static class SimpleOrdersResult {
        List<SimpleOrderDTO> orders;
    }

    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        SimpleOrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
