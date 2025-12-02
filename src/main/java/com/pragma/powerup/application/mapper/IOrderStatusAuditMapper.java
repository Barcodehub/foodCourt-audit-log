package com.pragma.powerup.application.mapper;

import com.pragma.powerup.apifirst.model.OrderStatusAuditRequestDto;
import com.pragma.powerup.apifirst.model.OrderStatusAuditResponseDto;
import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderStatusAuditMapper {

    OrderStatusAuditModel toModel(OrderStatusAuditRequestDto requestDto);

    OrderStatusAuditResponseDto toResponseDto(OrderStatusAuditModel model);
}

