package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.apifirst.model.OrderStatusAuditDataResponseDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditListResponseDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditRequestDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditResponseDto;
import com.pragma.powerup.apifirst.model.PaginationMetaDto;
import com.pragma.powerup.application.handler.IOrderStatusAuditHandler;
import com.pragma.powerup.application.mapper.IOrderStatusAuditMapper;
import com.pragma.powerup.domain.api.IOrderStatusAuditServicePort;
import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderStatusAuditHandler implements IOrderStatusAuditHandler {

    private final IOrderStatusAuditServicePort auditServicePort;
    private final IOrderStatusAuditMapper auditMapper;

    @Override
    public OrderStatusAuditDataResponseDto createAudit(OrderStatusAuditRequestDto requestDto) {
        OrderStatusAuditModel auditModel = auditMapper.toModel(requestDto);
        OrderStatusAuditModel savedAudit = auditServicePort.createAudit(auditModel);
        OrderStatusAuditResponseDto responseDto = auditMapper.toResponseDto(savedAudit);

        OrderStatusAuditDataResponseDto dataResponse = new OrderStatusAuditDataResponseDto();
        dataResponse.setData(responseDto);
        return dataResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatusAuditListResponseDto getAuditHistory(Long clientId, Long orderId, List<String> actionTypes, Pageable pageable) {
        Page<OrderStatusAuditModel> auditPage = auditServicePort.getAuditHistory(clientId, orderId, actionTypes, pageable);

        List<OrderStatusAuditResponseDto> auditList = auditPage.getContent()
                .stream()
                .map(auditMapper::toResponseDto)
                .collect(Collectors.toList());

        PaginationMetaDto meta = new PaginationMetaDto();
        meta.setPage(auditPage.getNumber());
        meta.setSize(auditPage.getSize());
        meta.setTotalElements((int) auditPage.getTotalElements());
        meta.setTotalPages(auditPage.getTotalPages());

        OrderStatusAuditListResponseDto response = new OrderStatusAuditListResponseDto();
        response.setData(auditList);
        response.setMeta(meta);

        return response;
    }
}

