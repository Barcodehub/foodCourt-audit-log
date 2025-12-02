package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderStatusAuditServicePort;
import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderStatusAuditUseCase implements IOrderStatusAuditServicePort {

    private final IOrderStatusAuditPersistencePort auditPersistencePort;

    @Override
    public OrderStatusAuditModel createAudit(OrderStatusAuditModel auditModel) {
        validateAuditModel(auditModel);

        if (auditModel.getChangedAt() == null) {
            auditModel.setChangedAt(LocalDateTime.now());
        }

        return auditPersistencePort.saveAudit(auditModel);
    }

    @Override
    public Page<OrderStatusAuditModel> getAuditHistory(Long clientId, Long orderId, List<String> actionTypes, Pageable pageable) {
        if (clientId == null && orderId == null && (actionTypes == null || actionTypes.isEmpty())) {
            throw new IllegalArgumentException("Al menos un filtro debe ser proporcionado");
        }

        return auditPersistencePort.findByFilters(clientId, orderId, actionTypes, pageable);
    }

    private void validateAuditModel(OrderStatusAuditModel auditModel) {
        if (auditModel.getOrderId() == null) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio");
        }
        if (auditModel.getRestaurantId() == null) {
            throw new IllegalArgumentException("El ID del restaurante es obligatorio");
        }
        if (auditModel.getClientId() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio");
        }
        if (auditModel.getNewStatus() == null || auditModel.getNewStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        if (auditModel.getChangedByUserId() == null) {
            throw new IllegalArgumentException("El ID del usuario que realizó el cambio es obligatorio");
        }
        if (auditModel.getChangedByRole() == null || auditModel.getChangedByRole().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol del usuario que realizó el cambio es obligatorio");
        }
    }
}

