package com.pragma.powerup.infrastructure.out.mongodb.mapper;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper entre el documento MongoDB y el modelo de dominio
 * Usa MapStruct para generación automática de código
 * Mantiene la separación entre la capa de infraestructura y dominio
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderStatusAuditDocumentMapper {

    /**
     * Convierte el modelo de dominio a documento MongoDB
     *
     * @param model Modelo de dominio
     * @return Documento MongoDB
     */
    OrderStatusAuditDocument toDocument(OrderStatusAuditModel model);

    /**
     * Convierte el documento MongoDB a modelo de dominio
     *
     * @param document Documento MongoDB
     * @return Modelo de dominio
     */
    OrderStatusAuditModel toModel(OrderStatusAuditDocument document);
}

