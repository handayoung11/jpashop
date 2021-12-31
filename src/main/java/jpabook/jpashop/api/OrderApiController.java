package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByOrderSearch(new OrderSearch());
        all.forEach(o -> {
            o.getMember().getName();
            o.getDelivery().getAddress();
            o.getOrderItems().forEach(i -> i.getItem().getName());
        });
        return all;
    }

    @GetMapping("/api/v2/orders")
    public OrderDTOResult ordersV2() {
        List<Order> orders = orderRepository.findAllByOrderSearch(new OrderSearch());
        List<OrderDTO> orderDTOList = orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return new OrderDTOResult(orderDTOList);
    }

    @Getter
    @AllArgsConstructor
    static class OrderDTOResult {
        List<OrderDTO> orders;
    }

    @Getter
    static class OrderDTO {
        Long orderId;
        String name;
        OrderStatus orderStatus;
        LocalDateTime orderDate;
        Address address;
        List<OrderItemDTO> orderItems;

        OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderStatus = order.getStatus();
            orderDate = order.getOrderDate();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDTO {
        String itemName;
        int orderPrice;
        int count;

        OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            ;
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
