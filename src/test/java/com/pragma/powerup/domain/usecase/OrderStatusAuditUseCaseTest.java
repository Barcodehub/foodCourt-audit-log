package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para OrderStatusAuditUseCase
 * Cubre la siguiente Historia de Usuario:
 * - HU17: Consultar trazabilidad del pedido
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderStatusAuditUseCase - Trazabilidad de Pedidos")
class OrderStatusAuditUseCaseTest {

    @Mock
    private IOrderStatusAuditPersistencePort auditPersistencePort;

    @InjectMocks
    private OrderStatusAuditUseCase auditUseCase;

    private OrderStatusAuditModel validAudit;

    @BeforeEach
    void setUp() {
        validAudit = new OrderStatusAuditModel();
        validAudit.setId("audit123");
        validAudit.setOrderId(1L);
        validAudit.setRestaurantId(10L);
        validAudit.setClientId(100L);
        validAudit.setPreviousStatus("PENDIENT");
        validAudit.setNewStatus("IN_PREPARE");
        validAudit.setChangedByUserId(200L);
        validAudit.setChangedByRole("EMPLEADO");
        validAudit.setActionType("ASSIGNMENT");
        validAudit.setEmployeeId(200L);
        validAudit.setNotes("Pedido asignado");
        validAudit.setChangedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("HU17: Consultar trazabilidad del pedido")
    class GetAuditHistoryTests {

        @Test
        @DisplayName("Happy Path: Debe obtener historial de auditoría de un pedido")
        void shouldGetAuditHistoryForOrder() {
            // Arrange
            Long orderId = 1L;
            Pageable pageable = PageRequest.of(0, 10);

            OrderStatusAuditModel audit1 = createAudit(1L, "PENDIENT", "IN_PREPARE");
            OrderStatusAuditModel audit2 = createAudit(1L, "IN_PREPARE", "READY");
            OrderStatusAuditModel audit3 = createAudit(1L, "READY", "DELIVERED");

            List<OrderStatusAuditModel> audits = Arrays.asList(audit1, audit2, audit3);
            Page<OrderStatusAuditModel> auditPage = new PageImpl<>(audits, pageable, audits.size());

            when(auditPersistencePort.findByFilters(null, orderId, null, pageable)).thenReturn(auditPage);

            // Act
            Page<OrderStatusAuditModel> result = auditUseCase.getAuditHistory(null, orderId, null, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(3, result.getTotalElements());
            assertEquals(3, result.getContent().size());

            verify(auditPersistencePort).findByFilters(null, orderId, null, pageable);
        }

        @Test
        @DisplayName("Validación: Debe filtrar auditoría por cliente")
        void shouldFilterAuditByClient() {
            // Arrange
            Long clientId = 100L;
            Pageable pageable = PageRequest.of(0, 10);

            OrderStatusAuditModel audit = createAudit(1L, "PENDIENT", "CANCELLED");
            audit.setClientId(clientId);

            Page<OrderStatusAuditModel> auditPage = new PageImpl<>(Arrays.asList(audit));

            when(auditPersistencePort.findByFilters(clientId, null, null, pageable)).thenReturn(auditPage);

            // Act
            Page<OrderStatusAuditModel> result = auditUseCase.getAuditHistory(clientId, null, null, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(clientId, result.getContent().get(0).getClientId());
        }

        @Test
        @DisplayName("Error: Debe retornar lista vacía cuando no hay auditorías")
        void shouldReturnEmptyListWhenNoAudits() {
            // Arrange
            Long orderId = 999L;
            Pageable pageable = PageRequest.of(0, 10);
            Page<OrderStatusAuditModel> emptyPage = new PageImpl<>(Arrays.asList());

            when(auditPersistencePort.findByFilters(null, orderId, null, pageable)).thenReturn(emptyPage);

            // Act
            Page<OrderStatusAuditModel> result = auditUseCase.getAuditHistory(null, orderId, null, pageable);

            // Assert
            assertNotNull(result);
            assertTrue(result.getContent().isEmpty());
        }

        @Test
        @DisplayName("Validación: Debe filtrar por tipo de acción")
        void shouldFilterByActionType() {
            // Arrange
            List<String> actionTypes = Arrays.asList("ASSIGNMENT", "READY_FOR_PICKUP");
            Pageable pageable = PageRequest.of(0, 10);

            OrderStatusAuditModel audit1 = createAudit(1L, "PENDIENT", "IN_PREPARE");
            audit1.setActionType("ASSIGNMENT");
            OrderStatusAuditModel audit2 = createAudit(1L, "IN_PREPARE", "READY");
            audit2.setActionType("READY_FOR_PICKUP");

            Page<OrderStatusAuditModel> auditPage = new PageImpl<>(Arrays.asList(audit1, audit2));

            when(auditPersistencePort.findByFilters(null, null, actionTypes, pageable)).thenReturn(auditPage);

            // Act
            Page<OrderStatusAuditModel> result = auditUseCase.getAuditHistory(null, null, actionTypes, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        }
    }

    @Nested
    @DisplayName("Crear Auditoría")
    class CreateAuditTests {

        @Test
        @DisplayName("Happy Path: Debe crear auditoría correctamente")
        void shouldCreateAuditSuccessfully() {
            // Arrange
            when(auditPersistencePort.saveAudit(any(OrderStatusAuditModel.class))).thenReturn(validAudit);

            // Act
            OrderStatusAuditModel result = auditUseCase.createAudit(validAudit);

            // Assert
            assertNotNull(result);
            verify(auditPersistencePort).saveAudit(validAudit);
        }

        @Test
        @DisplayName("Validación: Debe establecer fecha automáticamente si no está presente")
        void shouldSetDateAutomaticallyIfNotPresent() {
            // Arrange
            validAudit.setChangedAt(null);
            when(auditPersistencePort.saveAudit(any(OrderStatusAuditModel.class))).thenReturn(validAudit);

            // Act
            auditUseCase.createAudit(validAudit);

            // Assert
            verify(auditPersistencePort).saveAudit(argThat(audit -> audit.getChangedAt() != null));
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría sin orderId")
        void shouldRejectAuditWithoutOrderId() {
            // Arrange
            validAudit.setOrderId(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> auditUseCase.createAudit(validAudit)
            );

            assertTrue(exception.getMessage().contains("pedido"));
            verify(auditPersistencePort, never()).saveAudit(any());
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría sin restaurantId")
        void shouldRejectAuditWithoutRestaurantId() {
            // Arrange
            validAudit.setRestaurantId(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> auditUseCase.createAudit(validAudit)
            );

            assertTrue(exception.getMessage().contains("restaurante"));
            verify(auditPersistencePort, never()).saveAudit(any());
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría sin clientId")
        void shouldRejectAuditWithoutClientId() {
            // Arrange
            validAudit.setClientId(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> auditUseCase.createAudit(validAudit)
            );

            assertTrue(exception.getMessage().contains("cliente"));
            verify(auditPersistencePort, never()).saveAudit(any());
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría sin nuevo estado")
        void shouldRejectAuditWithoutNewStatus() {
            // Arrange
            validAudit.setNewStatus(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> auditUseCase.createAudit(validAudit));
            verify(auditPersistencePort, never()).saveAudit(any());
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría con nuevo estado vacío")
        void shouldRejectAuditWithEmptyNewStatus() {
            // Arrange
            validAudit.setNewStatus("");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> auditUseCase.createAudit(validAudit));
            verify(auditPersistencePort, never()).saveAudit(any());
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría sin changedByUserId")
        void shouldRejectAuditWithoutChangedByUserId() {
            // Arrange
            validAudit.setChangedByUserId(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> auditUseCase.createAudit(validAudit));
            verify(auditPersistencePort, never()).saveAudit(any());
        }

        @Test
        @DisplayName("Error: Debe rechazar auditoría sin changedByRole")
        void shouldRejectAuditWithoutChangedByRole() {
            // Arrange
            validAudit.setChangedByRole(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> auditUseCase.createAudit(validAudit));
            verify(auditPersistencePort, never()).saveAudit(any());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Edge Case: Auditoría con previousStatus null debe ser aceptada")
        void shouldAcceptAuditWithNullPreviousStatus() {
            // Arrange
            validAudit.setPreviousStatus(null); // Para creación de pedido
            when(auditPersistencePort.saveAudit(any(OrderStatusAuditModel.class))).thenReturn(validAudit);

            // Act
            OrderStatusAuditModel result = auditUseCase.createAudit(validAudit);

            // Assert
            assertNotNull(result);
            verify(auditPersistencePort).saveAudit(validAudit);
        }

        @Test
        @DisplayName("Edge Case: Auditoría con employeeId null debe ser aceptada")
        void shouldAcceptAuditWithNullEmployeeId() {
            // Arrange
            validAudit.setEmployeeId(null); // Para acciones de cliente
            when(auditPersistencePort.saveAudit(any(OrderStatusAuditModel.class))).thenReturn(validAudit);

            // Act
            OrderStatusAuditModel result = auditUseCase.createAudit(validAudit);

            // Assert
            assertNotNull(result);
            verify(auditPersistencePort).saveAudit(validAudit);
        }

        @Test
        @DisplayName("Edge Case: Debe manejar paginación correctamente")
        void shouldHandlePaginationCorrectly() {
            // Arrange
            Pageable page1 = PageRequest.of(0, 2);
            Pageable page2 = PageRequest.of(1, 2);

            List<OrderStatusAuditModel> auditsPage1 = Arrays.asList(
                createAudit(1L, "PENDIENT", "IN_PREPARE"),
                createAudit(1L, "IN_PREPARE", "READY")
            );

            List<OrderStatusAuditModel> auditsPage2 = Arrays.asList(
                createAudit(1L, "READY", "DELIVERED")
            );

            Page<OrderStatusAuditModel> auditPage1 = new PageImpl<>(auditsPage1, page1, 3);
            Page<OrderStatusAuditModel> auditPage2 = new PageImpl<>(auditsPage2, page2, 3);

            when(auditPersistencePort.findByFilters(null, 1L, null, page1)).thenReturn(auditPage1);
            when(auditPersistencePort.findByFilters(null, 1L, null, page2)).thenReturn(auditPage2);

            // Act
            Page<OrderStatusAuditModel> resultPage1 = auditUseCase.getAuditHistory(null, 1L, null, page1);
            Page<OrderStatusAuditModel> resultPage2 = auditUseCase.getAuditHistory(null, 1L, null, page2);

            // Assert
            assertEquals(2, resultPage1.getContent().size());
            assertEquals(1, resultPage2.getContent().size());
            assertTrue(resultPage1.hasNext());
            assertFalse(resultPage2.hasNext());
        }
    }

    // Método auxiliar
    private OrderStatusAuditModel createAudit(Long orderId, String previousStatus, String newStatus) {
        OrderStatusAuditModel audit = new OrderStatusAuditModel();
        audit.setOrderId(orderId);
        audit.setRestaurantId(10L);
        audit.setClientId(100L);
        audit.setPreviousStatus(previousStatus);
        audit.setNewStatus(newStatus);
        audit.setChangedByUserId(200L);
        audit.setChangedByRole("EMPLEADO");
        audit.setActionType("STATUS_CHANGE");
        audit.setChangedAt(LocalDateTime.now());
        return audit;
    }
}

