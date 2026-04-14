package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.invoiceline.InvoiceLineDto;
import com.tavarlabs.pos.entity.InvoiceLine;
import com.tavarlabs.pos.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceLineMapper {

    @Mapping(target = "total", source = ".", qualifiedByName = "calculateLineTotal")
    InvoiceLineDto toDto(InvoiceLine line);

    @Named("calculateLineTotal")
    default double calculateLineTotal(InvoiceLine line){
        if(line.getProduct() == null) return 0;
        double result = line.getProduct().getPrice() * line.getQuantity();
        return result;
    }
}
