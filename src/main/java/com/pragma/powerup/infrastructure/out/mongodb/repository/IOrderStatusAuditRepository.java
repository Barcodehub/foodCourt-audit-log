package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Repositorio MongoDB para auditoría de cambios de estado
 * Extiende MongoRepository para operaciones CRUD básicas
 * Incluye queries personalizadas para búsquedas complejas
 */
public interface IOrderStatusAuditRepository extends MongoRepository<OrderStatusAuditDocument, String> {

    /**
     * Encuentra auditorías aplicando múltiples filtros opcionales
     * Los resultados se ordenan por fecha de cambio descendente
     *
     * @param clientId ID del cliente (opcional)
     * @param orderId ID del pedido (opcional)
     * @param actionTypes Lista de tipos de acción (opcional)
     * @param pageable Configuración de paginación
     * @return Página de documentos que cumplen los criterios
     */
    @Query("{ " +
            "$and: [ " +
            "  { $or: [ { 'client_id': ?0 }, { $expr: { $eq: [?0, null] } } ] }, " +
            "  { $or: [ { 'order_id': ?1 }, { $expr: { $eq: [?1, null] } } ] }, " +
            "  { $or: [ { 'action_type': { $in: ?2 } }, { $expr: { $eq: [?2, null] } }, { $expr: { $eq: [{ $size: ?2 }, 0] } } ] } " +
            "] " +
            "}")
    Page<OrderStatusAuditDocument> findByFilters(Long clientId, Long orderId, List<String> actionTypes, Pageable pageable);

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
}

