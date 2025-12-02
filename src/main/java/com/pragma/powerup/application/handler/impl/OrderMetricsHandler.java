package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.apifirst.model.*;
import com.pragma.powerup.application.handler.IOrderMetricsHandler;
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
public class OrderMetricsHandler implements IOrderMetricsHandler {

    private final IOrderStatusAuditServicePort auditServicePort;
    private final IOrderStatusAuditMapper auditMapper;

//--> Metricsusecase
}

