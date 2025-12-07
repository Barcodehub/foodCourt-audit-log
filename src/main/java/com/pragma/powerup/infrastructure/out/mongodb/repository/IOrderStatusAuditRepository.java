package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderStatusAuditRepository extends MongoRepository<OrderStatusAuditDocument, String>, OrderStatusAuditRepositoryCustom {

    Page<OrderStatusAuditDocument> findByClientId(Long clientId, Pageable pageable);

    Page<OrderStatusAuditDocument> findByOrderId(Long orderId, Pageable pageable);


    @Query("{ 'restaurant_id': ?0, 'new_status': { $regex: ?1, $options: 'i' }, " +
            "'changed_at': { $gte: ?2, $lte: ?3 } }")
    List<OrderStatusAuditDocument> findByRestaurantAndStatusAndDateRange(
            Long restaurantId,
            String newStatus,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("{ 'restaurant_id': ?0, 'new_status': { $in: ?1 }, " +
            "'changed_at': { $gte: ?2, $lte: ?3 } }")
    List<OrderStatusAuditDocument> findByRestaurantAndStatusInAndDateRange(
            Long restaurantId,
            List<String> statuses,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("{ 'restaurant_id': ?0, 'new_status': { $in: ?1 }, " +
            "'changed_at': { $gte: ?2, $lte: ?3 } }")
    Page<OrderStatusAuditDocument> findByRestaurantAndStatusInAndDateRangePaged(
            Long restaurantId,
            List<String> statuses,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    List<OrderStatusAuditDocument> findByOrderIdOrderByChangedAtAsc(Long orderId);
}



