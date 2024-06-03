package com.promocodes.promocodesmanager.report.sales;

import com.promocodes.promocodesmanager.purchase.Purchase;
import com.promocodes.promocodesmanager.purchase.PurchaseService;
import com.promocodes.promocodesmanager.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesReportService extends ReportService {
    private final PurchaseService purchaseService;

    @Autowired
    public SalesReportService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public SalesReportByCurrencyResponseDto generateSalesByCurrencyReport(
            LocalDate startDate, LocalDate endDate) {
        validateReportDates(startDate, endDate);

        SalesReportByCurrencyResponseDto responseDto =
                new SalesReportByCurrencyResponseDto(startDate, endDate);

        List<List<Purchase>> purchasesGroupedByCurrency =
                purchaseService.findPurchasesGroupedByCurrency();

        for (List<Purchase> purchases : purchasesGroupedByCurrency) {
            int numberOfPurchases = 0;
            double totalRevenue = 0.0;
            double totalDiscount = 0.0;

            for (Purchase purchase : purchases) {
                LocalDate purchaseDate = purchase.getPurchaseDate();

                if (purchaseDate.isBefore(startDate)
                        || purchaseDate.isAfter(endDate)) {
                    continue;
                }

                numberOfPurchases++;
                totalRevenue += purchase.getRegularPrice();
                totalDiscount += purchase.getDiscountAmount();
            }

            SalesReportByCurrencyEntryDto dto =
                    new SalesReportByCurrencyEntryDto();

            dto.setCurrency(purchases.get(0).getCurrency());
            dto.setNumberOfPurchases(numberOfPurchases);
            dto.setTotalRevenue(totalRevenue);
            dto.setTotalDiscount(totalDiscount);
            dto.setTotalNetRevenue(totalRevenue - totalDiscount);

            responseDto.getEntries().add(dto);
        }

        return responseDto;
    }

}
