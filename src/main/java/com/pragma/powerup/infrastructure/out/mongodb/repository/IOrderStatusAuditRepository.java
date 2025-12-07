package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio MongoDB para auditoría de cambios de estado
 * Extiende MongoRepository para operaciones CRUD básicas
 * Incluye queries personalizadas para búsquedas complejas
 */
public interface IOrderStatusAuditRepository extends MongoRepository<OrderStatusAuditDocument, String>, OrderStatusAuditRepositoryCustom {

    /**
     * Encuentra todas las auditorías de un cliente específico
     *
     * @param clientId ID del cliente
     * @param pageable Configuración de paginación
     * @return Página de documentos del cliente
     */
    Page<OrderStatusAuditDocument> findByClientId(Long clientId, Pageable pageable);

    /**
     * Encuentra todas las auditorías de un pedido específico
     *
     * @param orderId ID del pedido
     * @param pageable Configuración de paginación
     * @return Página de documentos del pedido
     */
    Page<OrderStatusAuditDocument> findByOrderId(Long orderId, Pageable pageable);

    /**
     * Encuentra todas las auditorías de un restaurante en un rango de fechas
     * Filtrado por estado "Pendiente" (inicio de pedido)
     *
     * @param restaurantId ID del restaurante
     * @param newStatus Estado nuevo (Pendiente, PENDIENTE, etc.)
     * @param startDate Fecha de inicio (opcional)
     * @param endDate Fecha de fin (opcional)
     * @return Lista de documentos que cumplen los criterios
     */
    @Query("{ 'restaurant_id': ?0, 'new_status': { $regex: ?1, $options: 'i' }, " +
            "'changed_at': { $gte: ?2, $lte: ?3 } }")
    List<OrderStatusAuditDocument> findByRestaurantAndStatusAndDateRange(
            Long restaurantId,
            String newStatus,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Encuentra todas las auditorías de un restaurante en un rango de fechas
     * Filtrado por estados finales (Entregado o Cancelado)
     *
     * @param restaurantId ID del restaurante
     * @param statuses Lista de estados finales
     * @param startDate Fecha de inicio (opcional)
     * @param endDate Fecha de fin (opcional)
     * @return Lista de documentos que cumplen los criterios
     */
    @Query("{ 'restaurant_id': ?0, 'new_status': { $in: ?1 }, " +
            "'changed_at': { $gte: ?2, $lte: ?3 } }")
    List<OrderStatusAuditDocument> findByRestaurantAndStatusInAndDateRange(
            Long restaurantId,
            List<String> statuses,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * Encuentra todos los cambios de estado de un pedido específico
     *
     * @param orderId ID del pedido
     * @return Lista de documentos ordenados por fecha
     */
    List<OrderStatusAuditDocument> findByOrderIdOrderByChangedAtAsc(Long orderId);
}



