package com.tavarlabs.pos.mappers;

import com.tavarlabs.pos.dtos.purchase.PurchaseDto;
import com.tavarlabs.pos.dtos.purchase.PurchaseWithoutLinesDto;
import com.tavarlabs.pos.entity.Purchase;
import com.tavarlabs.pos.entity.PurchaseLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = PurchaseLineMapper.class
)
public interface PurchaseMapper {
    @Mapping(target = "total", source = ".", qualifiedByName = "calculatePurchaseTotal")
    PurchaseDto toDto(Purchase purchase);

    @Mapping(target = "total", source = ".", qualifiedByName = "calculatePurchaseTotal")
    PurchaseWithoutLinesDto toPurchaseWithoutLinesDto(Purchase purchase);

    @Named("calculatePurchaseTotal")
    default double calculatePurchaseTotal(Purchase purchase){
        if(purchase.getLines() == null) return 0;
        double result = 0;
        for(PurchaseLine line : purchase.getLines()){
            result += line.getProduct().getPrice() * line.getQuantity();
        }
        return result;
    }
}
