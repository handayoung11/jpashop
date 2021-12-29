package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.dto.api.SimpleOrderQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<SimpleOrderQueryDTO> findSimpleOrderQueryDTOList() {
        return em.createQuery("select new jpabook.jpashop.dto.api.SimpleOrderQueryDTO(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o " +
                        "inner join o.member m " +
                        "inner join o.delivery d", SimpleOrderQueryDTO.class)
                .getResultList();
    }
}
