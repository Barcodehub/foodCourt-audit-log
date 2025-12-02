package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.apifirst.api.AuditApi;
import com.pragma.powerup.apifirst.model.OrderStatusAuditDataResponseDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditListResponseDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditRequestDto;
import com.pragma.powerup.application.handler.IOrderStatusAuditHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderStatusAuditController implements AuditApi {

    private final IOrderStatusAuditHandler auditHandler;

    @Override
    public ResponseEntity<OrderStatusAuditDataResponseDto> createOrderStatusAudit(OrderStatusAuditRequestDto orderStatusAuditRequestDto) {
        OrderStatusAuditDataResponseDto response = auditHandler.createAudit(orderStatusAuditRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<OrderStatusAuditListResponseDto> getOrderStatusAuditHistory(
            Long clientId,
            Long orderId,
            List<String> actionTypes,
            Integer page,
            Integer size) {

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 10
        );

        OrderStatusAuditListResponseDto response = auditHandler.getAuditHistory(clientId, orderId, actionTypes, pageable);
        return ResponseEntity.ok(response);
    }
}

