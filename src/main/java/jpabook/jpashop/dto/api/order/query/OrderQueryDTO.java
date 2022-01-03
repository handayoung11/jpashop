package jpabook.jpashop.dto.api.order.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderQueryDTO {
    private Long orderId;
    private String name;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private Address address;
    private List<OrderItemQueryDTO> orderItems;

    public OrderQueryDTO(Long orderId, String name, Address address, OrderStatus orderStatus, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.name = name;
        this.address = address;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }
}
