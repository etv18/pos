package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.invoice.InvoiceDto;
import com.tavarlabs.pos.dtos.invoice.InvoiceWithoutLinesDto;
import com.tavarlabs.pos.entity.Invoice;
import com.tavarlabs.pos.entity.InvoiceLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = InvoiceLineMapper.class
)
public interface InvoiceMapper {
    @Mapping(target = "total", source = ".", qualifiedByName = "calculateInvoiceTotal")
    InvoiceDto toDto(Invoice invoice);

    @Mapping(target = "total", source = ".", qualifiedByName = "calculateInvoiceTotal")
    InvoiceWithoutLinesDto toInvoiceWithoutLinesDto(Invoice invoice);

    @Named("calculateInvoiceTotal")
    default double calculateInvoiceTotal(Invoice invoice){
        if(invoice.getLines() == null) return 0;
        double result = 0;
        for(InvoiceLine line : invoice.getLines()){
            result += line.getProduct().getPrice() * line.getQuantity();
        }
        return result;
    }
}
