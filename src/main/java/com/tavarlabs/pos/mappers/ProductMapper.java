package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.product.ProductDto;
import com.tavarlabs.pos.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductDto toDto(Product product);
}
