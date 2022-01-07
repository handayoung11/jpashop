package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.api.order.query.OrderFlatDTO;
import jpabook.jpashop.dto.api.order.query.OrderItemQueryDTO;
import jpabook.jpashop.dto.api.order.query.OrderQueryDTO;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderQueryService orderQueryService;

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

    // OSIV OFF에서도 동작
    @GetMapping("/api/v2/orders")
    public OrderDTOResult ordersV2() {
        List<Order> orders = orderQueryService.findAllWithLazyLoadMemAndAddAndOrI();
        List<OrderDTO> orderDTOList = orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return new OrderDTOResult(orderDTOList);
    }

    @GetMapping("/api/v3/orders")
    public OrderDTOResult ordersV3() {
        List<Order> orders = orderRepository.findAllWithItemAndDeliveryAndMember();
        return new OrderDTOResult(orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/v3.1/orders")
    public OrderDTOResult ordersV3Page(@RequestParam(defaultValue = "0") int offset,
                                       @RequestParam(defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberAndDelivery(offset, limit);
        return new OrderDTOResult(orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/v4/orders")
    public OrderDTOResult ordersV4() {
        return new OrderDTOResult(orderQueryRepository.findOrderQueryDTOList());
    }

    @GetMapping("/api/v5/orders")
    public OrderDTOResult ordersV5() {
        return new OrderDTOResult(orderQueryRepository.findOrderQueryDTOListUsingIn());
    }

    @GetMapping("/api/v6/orders")
    public OrderDTOResult ordersV6() {
        List<OrderQueryDTO> result = orderQueryRepository.findOrderFlatDTOList().stream()
                .collect(Collectors.groupingBy(OrderFlatDTO::getOrderId))
                .entrySet().stream()
                .map(o -> {
                    List<OrderFlatDTO> list = o.getValue();
                    OrderFlatDTO flatDTO = list.get(0);
                    OrderQueryDTO queryDTO = new OrderQueryDTO(flatDTO.getOrderId(), flatDTO.getName(), flatDTO.getAddress(), flatDTO.getOrderStatus(), flatDTO.getOrderDate());
                    queryDTO.setOrderItems(new ArrayList<>());
                    list.forEach(item -> queryDTO.getOrderItems().add(new OrderItemQueryDTO(item.getOrderId(), item.getItemName(), item.getOrderPrice(), item.getCount())));
                    return queryDTO;
                }).collect(Collectors.toList());
        return new OrderDTOResult(result);
    }

    @Getter
    @AllArgsConstructor
    static class OrderDTOResult<T> {
        List<T> orders;
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
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
