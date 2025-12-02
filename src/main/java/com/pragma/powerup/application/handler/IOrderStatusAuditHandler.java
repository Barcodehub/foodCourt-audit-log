package com.pragma.powerup.application.handler;

import com.pragma.powerup.apifirst.model.OrderStatusAuditDataResponseDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditListResponseDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderStatusAuditHandler {

    OrderStatusAuditDataResponseDto createAudit(OrderStatusAuditRequestDto requestDto);

    OrderStatusAuditListResponseDto getAuditHistory(Long clientId, Long orderId, List<String> actionTypes, Pageable pageable);
}

