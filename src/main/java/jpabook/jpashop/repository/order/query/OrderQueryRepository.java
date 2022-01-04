    package jpabook.jpashop.repository.order.query;

    import jpabook.jpashop.dto.api.order.query.OrderItemQueryDTO;
    import jpabook.jpashop.dto.api.order.query.OrderQueryDTO;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Repository;

    import javax.persistence.EntityManager;
    import java.util.List;

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
