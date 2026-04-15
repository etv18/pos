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

    //@Mapping(target = "total", expression = "java(calculateLineTotal(line))")
    @Mapping(target = "total", source = ".", qualifiedByName = "calculateLineTotal")
    InvoiceLineDto toDto(InvoiceLine line);

    @Named("calculateLineTotal")
    default Double calculateLineTotal(InvoiceLine line){
        if(line.getProduct() == null)
            throw new RuntimeException("No product was found when mapping to InvoiceLineDto");
        Double result = line.getProduct().getPrice() * line.getQuantity();
        return result;
    }
}
