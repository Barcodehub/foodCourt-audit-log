package com.pragma.powerup.infrastructure.out.mongodb.adapter;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import com.pragma.powerup.infrastructure.out.mongodb.mapper.IOrderStatusAuditDocumentMapper;
import com.pragma.powerup.infrastructure.out.mongodb.repository.IOrderStatusAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Adaptador que implementa el puerto de persistencia usando MongoDB
 * Responsabilidad: Traducir operaciones del dominio a operaciones de MongoDB
 * Sigue el principio de inversión de dependencias (SOLID)
 */
@Service
@RequiredArgsConstructor
public class OrderStatusAuditMongoAdapter implements IOrderStatusAuditPersistencePort {

    private final IOrderStatusAuditRepository auditRepository;
    private final IOrderStatusAuditDocumentMapper auditMapper;

    /**
     * Guarda una auditoría en MongoDB
     *
     * @param auditModel Modelo de dominio a guardar
     * @return Modelo guardado con ID generado
     */
    @Override
    public OrderStatusAuditModel saveAudit(OrderStatusAuditModel auditModel) {
        OrderStatusAuditDocument document = auditMapper.toDocument(auditModel);
        OrderStatusAuditDocument savedDocument = auditRepository.save(document);
        return auditMapper.toModel(savedDocument);
    }

    /**
     * Busca auditorías aplicando filtros opcionales
     *
     * @param clientId ID del cliente (opcional)
     * @param orderId ID del pedido (opcional)
     * @param actionTypes Lista de tipos de acción (opcional)
     * @param pageable Configuración de paginación
     * @return Página de modelos que cumplen los criterios
     */
    @Override
    public Page<OrderStatusAuditModel> findByFilters(Long clientId, Long orderId,
                                                      List<String> actionTypes, Pageable pageable) {
        Page<OrderStatusAuditDocument> documents = auditRepository.findByFilters(
                clientId, orderId, actionTypes, pageable);
        return documents.map(auditMapper::toModel);
    }

    /**
     * Busca auditorías por ID de cliente
     *
     * @param clientId ID del cliente
     * @param pageable Configuración de paginación
     * @return Página de modelos del cliente
     */
    @Override
    public Page<OrderStatusAuditModel> findByClientId(Long clientId, Pageable pageable) {
        Page<OrderStatusAuditDocument> documents = auditRepository.findByClientId(clientId, pageable);
        return documents.map(auditMapper::toModel);
    }
}

