package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.dto.api.order.query.OrderItemQueryDTO;
import jpabook.jpashop.dto.api.order.query.OrderQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDTO> findOrderQueryDTOList() {
        List<OrderQueryDTO> result = findOrdersWithMemberAndDelivery();
        for (OrderQueryDTO o : result) {
            List<OrderItemQueryDTO> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        }
        return result;
    }

    public List<OrderQueryDTO> findOrderQueryDTOListUsingIn() {
        List<OrderQueryDTO> result = findOrdersWithMemberAndDelivery();
        List<Long> orderIds = getOrderIds(result);
        Map<Long, List<OrderItemQueryDTO>> collect = getOrderItemQueryDTOMap(orderIds);
        result.forEach(o -> o.setOrderItems(collect.get(o.getOrderId())));
        return result;
    }

    private List<Long> getOrderIds(List<OrderQueryDTO> result) {
        return result.stream()
                .map(OrderQueryDTO::getOrderId)
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDTO>> getOrderItemQueryDTOMap(List<Long> orderIds) {
        List<OrderItemQueryDTO> orderItemQueryDTO =
                em.createQuery("select new jpabook.jpashop.dto.api.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id in (:orderIds)", OrderItemQueryDTO.class)
                        .setParameter("orderIds", orderIds)
                        .getResultList();
        return orderItemQueryDTO.stream().collect(Collectors.groupingBy(OrderItemQueryDTO::getOrderId));
    }

    private List<OrderItemQueryDTO> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.dto.api.order.query.OrderItemQueryDTO(oi.order.id, oi.item.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item " +
                        "where oi.order.id = :orderId")
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDTO> findOrdersWithMemberAndDelivery() {
        return em.createQuery("select new jpabook.jpashop.dto.api.order.query.OrderQueryDTO(o.id, m.name, d.address, o.status, o.orderDate) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderQueryDTO.class)
                .getResultList();
    }
}
