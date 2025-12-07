package com.pragma.powerup.infrastructure.out.mongodb.mapper;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderStatusAuditDocumentMapper {

    OrderStatusAuditDocument toDocument(OrderStatusAuditModel model);

    OrderStatusAuditModel toModel(OrderStatusAuditDocument document);
}

