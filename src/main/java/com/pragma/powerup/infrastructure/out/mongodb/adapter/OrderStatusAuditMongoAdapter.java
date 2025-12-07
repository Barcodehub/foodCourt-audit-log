package com.pragma.powerup.infrastructure.out.mongodb.adapter;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.domain.spi.IOrderMetricsPersistencePort;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import com.pragma.powerup.infrastructure.out.mongodb.mapper.IOrderStatusAuditDocumentMapper;
import com.pragma.powerup.infrastructure.out.mongodb.repository.IOrderStatusAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderStatusAuditMongoAdapter implements IOrderStatusAuditPersistencePort, IOrderMetricsPersistencePort {

    private final IOrderStatusAuditRepository auditRepository;
    private final IOrderStatusAuditDocumentMapper auditMapper;

    @Override
    public OrderStatusAuditModel saveAudit(OrderStatusAuditModel auditModel) {
        OrderStatusAuditDocument document = auditMapper.toDocument(auditModel);
        OrderStatusAuditDocument savedDocument = auditRepository.save(document);
        return auditMapper.toModel(savedDocument);
    }

    @Override
    public Page<OrderStatusAuditModel> findByFilters(Long clientId, Long orderId,
                                                      List<String> actionTypes, Pageable pageable) {
        Page<OrderStatusAuditDocument> documents = auditRepository.findByFilters(
                clientId, orderId, actionTypes, pageable);
        return documents.map(auditMapper::toModel);
    }

    @Override
    public Page<OrderStatusAuditModel> findByClientId(Long clientId, Pageable pageable) {
        Page<OrderStatusAuditDocument> documents = auditRepository.findByClientId(clientId, pageable);
        return documents.map(auditMapper::toModel);
    }

    @Override
    public Page<OrderStatusAuditModel> findByRestaurantAndDateRange(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ) {
        // Establecer fechas por defecto si no se proporcionan
        LocalDateTime effectiveStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(1);
        LocalDateTime effectiveEndDate = endDate != null ? endDate : LocalDateTime.now();

        // Obtener los estados finales (Entregado o Cancelado)
        List<String> finalStatuses = Arrays.asList(
                "ENTREGADO", "Entregado", "DELIVERED",
                "CANCELADO", "Cancelado", "CANCELLED"
        );

        Page<OrderStatusAuditDocument> documents = auditRepository.findByRestaurantAndStatusInAndDateRangePaged(
                restaurantId, finalStatuses, effectiveStartDate, effectiveEndDate, pageable
        );

        return documents.map(auditMapper::toModel);
    }

    @Override
    public List<OrderStatusAuditModel> findByOrderId(Long orderId) {
        List<OrderStatusAuditDocument> documents = auditRepository.findByOrderIdOrderByChangedAtAsc(orderId);
        return documents.stream()
                .map(auditMapper::toModel)
               .toList();
    }
}

