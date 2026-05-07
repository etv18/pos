package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.purchaseline.PurchaseLineDto;
import com.tavarlabs.pos.entity.PurchaseLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseLineMapper {

    //@Mapping(target = "total", expression = "java(calculateLineTotal(line))")
    @Mapping(target = "total", source = ".", qualifiedByName = "calculateLineTotal")
    PurchaseLineDto toDto(PurchaseLine line);

    @Named("calculateLineTotal")
    default Double calculateLineTotal(PurchaseLine line){
        if(line.getProduct() == null)
            throw new RuntimeException("No product was found when mapping to PurchaseLineDto");
        Double result = line.getProduct().getPrice() * line.getQuantity();
        return result;
    }
}
