package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación personalizada del repositorio de auditoría
 * Permite construir queries dinámicas basadas en filtros opcionales
 */
@Repository
@RequiredArgsConstructor
public class OrderStatusAuditRepositoryCustomImpl implements OrderStatusAuditRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderStatusAuditDocument> findByFilters(Long clientId, Long orderId,
                                                         List<String> actionTypes, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (clientId != null) {
            criteriaList.add(Criteria.where("client_id").is(clientId));
        }

        if (orderId != null) {
            criteriaList.add(Criteria.where("order_id").is(orderId));
        }

        if (actionTypes != null && !actionTypes.isEmpty()) {
            criteriaList.add(Criteria.where("action_type").in(actionTypes));
        }

        // Combinar todos los filtros existente
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.with(pageable);

        long total = mongoTemplate.count(query, OrderStatusAuditDocument.class);
        List<OrderStatusAuditDocument> documents = mongoTemplate.find(query, OrderStatusAuditDocument.class);

        return new PageImpl<>(documents, pageable, total);
    }
}

