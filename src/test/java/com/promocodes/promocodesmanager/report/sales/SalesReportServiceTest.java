package com.promocodes.promocodesmanager.report.sales;

import com.promocodes.promocodesmanager.exception.FailedToGenerateReportException;
import com.promocodes.promocodesmanager.purchase.Purchase;
import com.promocodes.promocodesmanager.purchase.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesReportServiceTest {
    @Mock
    private PurchaseService purchaseService;

    private SalesReportService salesReportService;

    @BeforeEach
    void setUp() {
        salesReportService = new SalesReportService(purchaseService);
    }

    @Test
    public void shouldGenerateSalesByCurrencyReportWithOneCurrencyEntry() {
        LocalDate startDate = LocalDate.now().minusDays(60);
        LocalDate endDate = LocalDate.now();

        Purchase purchaseUSD1 = new Purchase(
                123L,
                "NikeShoes",
                LocalDate.now().minusDays(30),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD2 = new Purchase(
                133L,
                "NikeShoes",
                LocalDate.now().minusDays(20),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD3 = new Purchase(
                143L,
                "NikeShoes",
                LocalDate.now().minusDays(10),
                150.0,
                50.0,
                "USD"
        );

        List<Purchase> purchasesUSD = List.of(
                purchaseUSD1,
                purchaseUSD2,
                purchaseUSD3
        );

        List<List<Purchase>> purchasesGroupedByCurrency =
                List.of(purchasesUSD);

        when(purchaseService.findPurchasesGroupedByCurrency())
                .thenReturn(purchasesGroupedByCurrency);

        SalesReportByCurrencyResponseDto reportResponseDto =
                salesReportService.generateSalesByCurrencyReport(
                        startDate,
                        endDate
                );

        assertNotNull(reportResponseDto);
        assertThat("SALES").isEqualTo(reportResponseDto.getCategory());
        assertThat("BY_CURRENCY").isEqualTo(reportResponseDto.getName());
        assertThat(startDate).isEqualTo(reportResponseDto.getStartDate());
        assertThat(endDate).isEqualTo(reportResponseDto.getEndDate());

        List<SalesReportByCurrencyEntryDto> entries =
                reportResponseDto.getEntries();
        assertThat(1).isEqualTo(entries.size());

        SalesReportByCurrencyEntryDto usdEntry = entries.get(0);
        assertThat("USD").isEqualTo(usdEntry.getCurrency());
        assertThat(3).isEqualTo(usdEntry.getNumberOfPurchases());
        assertThat(450.0).isEqualTo(usdEntry.getTotalRevenue());
        assertThat(150.0).isEqualTo(usdEntry.getTotalDiscount());
        assertThat(300.0).isEqualTo(usdEntry.getTotalNetRevenue());

        verify(purchaseService, times(1))
                .findPurchasesGroupedByCurrency();
    }

    @Test
    public void shouldGenerateSalesByCurrencyReportWithMultipleCurrenciesEntries() {
        LocalDate startDate = LocalDate.now().minusDays(60);
        LocalDate endDate = LocalDate.now();

        Purchase purchaseUSD1 = new Purchase(
                123L,
                "NikeShoes",
                LocalDate.now().minusDays(30),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD2 = new Purchase(
                133L,
                "NikeShoes",
                LocalDate.now().minusDays(20),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseEUR1 = new Purchase(
                143L,
                "AdidasShoes",
                LocalDate.now().minusDays(10),
                250.0,
                150.0,
                "EUR"
        );

        Purchase purchasePLN1 = new Purchase(
                163L,
                "PumaShoes",
                LocalDate.now().minusDays(8),
                250.0,
                40.0,
                "PLN"
        );

        Purchase purchasePLN2 = new Purchase(
                173L,
                "PumaShoes",
                LocalDate.now().minusDays(6),
                250.0,
                40.0,
                "PLN"
        );

        Purchase purchasePLN3 = new Purchase(
                183L,
                "PumaShoes",
                LocalDate.now().minusDays(4),
                250.0,
                40.0,
                "PLN"
        );

        List<Purchase> purchasesUSD = List.of(
                purchaseUSD1,
                purchaseUSD2
        );

        List<Purchase> purchasesEUR = List.of(
                purchaseEUR1
        );

        List<Purchase> purchasesPLN = List.of(
                purchasePLN1,
                purchasePLN2,
                purchasePLN3
        );

        List<List<Purchase>> purchasesGroupedByCurrency = List.of(
                purchasesUSD,
                purchasesEUR,
                purchasesPLN
        );

        when(purchaseService.findPurchasesGroupedByCurrency())
                .thenReturn(purchasesGroupedByCurrency);

        SalesReportByCurrencyResponseDto reportResponseDto =
                salesReportService.generateSalesByCurrencyReport(
                        startDate,
                        endDate
                );

        assertNotNull(reportResponseDto);
        assertThat("SALES").isEqualTo(reportResponseDto.getCategory());
        assertThat("BY_CURRENCY").isEqualTo(reportResponseDto.getName());
        assertThat(startDate).isEqualTo(reportResponseDto.getStartDate());
        assertThat(endDate).isEqualTo(reportResponseDto.getEndDate());

        assertThat(3)
                .isEqualTo(reportResponseDto.getEntries().size());

        Set<String> expectedCurrenciesInResponseDto = new HashSet<>();
        expectedCurrenciesInResponseDto.add("USD");
        expectedCurrenciesInResponseDto.add("EUR");
        expectedCurrenciesInResponseDto.add("PLN");

        for (SalesReportByCurrencyEntryDto report : reportResponseDto.getEntries()) {

            if (report.getCurrency().equals("USD")) {
                assertThat(expectedCurrenciesInResponseDto).contains("USD");

                assertThat(2)
                        .isEqualTo(report.getNumberOfPurchases());
                assertThat(300.0)
                        .isEqualTo(report.getTotalRevenue());
                assertThat(100.0)
                        .isEqualTo(report.getTotalDiscount());
                assertThat(200.0)
                        .isEqualTo(report.getTotalNetRevenue());

                expectedCurrenciesInResponseDto.remove("USD");
            }

            if (report.getCurrency().equals("EUR")) {
                assertThat(expectedCurrenciesInResponseDto).contains("EUR");

                assertThat(1)
                        .isEqualTo(report.getNumberOfPurchases());
                assertThat(250.0)
                        .isEqualTo(report.getTotalRevenue());
                assertThat(150.0)
                        .isEqualTo(report.getTotalDiscount());
                assertThat(100.0)
                        .isEqualTo(report.getTotalNetRevenue());

                expectedCurrenciesInResponseDto.remove("EUR");
            }

            if (report.getCurrency().equals("PLN")) {
                assertThat(expectedCurrenciesInResponseDto).contains("PLN");

                assertThat(3)
                        .isEqualTo(report.getNumberOfPurchases());
                assertThat(750.0)
                        .isEqualTo(report.getTotalRevenue());
                assertThat(120.0)
                        .isEqualTo(report.getTotalDiscount());
                assertThat(630.0)
                        .isEqualTo(report.getTotalNetRevenue());

                expectedCurrenciesInResponseDto.remove("PLN");
            }
        }

        assertThat(0)
                .isEqualTo(expectedCurrenciesInResponseDto.size());

        verify(purchaseService, times(1))
                .findPurchasesGroupedByCurrency();
    }

    @Test
    public void shouldGenerateEmptySalesByCurrencyReportIfNoPurchaseIsFound() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        when(purchaseService.findPurchasesGroupedByCurrency())
                .thenReturn(Collections.emptyList());

        SalesReportByCurrencyResponseDto reportResponseDto =
                salesReportService.generateSalesByCurrencyReport(
                        startDate,
                        endDate
                );

        assertNotNull(reportResponseDto);
        assertThat("SALES")
                .isEqualTo(reportResponseDto.getCategory());
        assertThat("BY_CURRENCY")
                .isEqualTo(reportResponseDto.getName());
        assertThat(startDate)
                .isEqualTo(reportResponseDto.getStartDate());
        assertThat(endDate)
                .isEqualTo(reportResponseDto.getEndDate());
        assertThat(0)
                .isEqualTo(reportResponseDto.getEntries().size());

        verify(purchaseService, times(1))
                .findPurchasesGroupedByCurrency();
    }

    @Test
    public void shouldGenerateSalesByCurrencyReportOnlyForPurchasesWithinReportDatesRange() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(10);

        Purchase purchaseUSD1 = new Purchase(
                123L,
                "NikeShoes",
                LocalDate.now().minusDays(40),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD2 = new Purchase(
                133L,
                "NikeShoes",
                LocalDate.now().minusDays(20),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD3 = new Purchase(
                143L,
                "NikeShoes",
                LocalDate.now().minusDays(15),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD4 = new Purchase(
                153L,
                "NikeShoes",
                LocalDate.now().minusDays(5),
                150.0,
                50.0,
                "USD"
        );

        List<Purchase> purchasesUSD = List.of(
                purchaseUSD1,
                purchaseUSD2,
                purchaseUSD3,
                purchaseUSD4
        );

        List<List<Purchase>> purchasesGroupedByCurrency = List.of(
                purchasesUSD
        );

        when(purchaseService.findPurchasesGroupedByCurrency())
                .thenReturn(purchasesGroupedByCurrency);

        SalesReportByCurrencyResponseDto reportResponseDto =
                 salesReportService.generateSalesByCurrencyReport(
                         startDate,
                         endDate
                 );

        assertNotNull(reportResponseDto);
        assertThat("SALES").isEqualTo(reportResponseDto.getCategory());
        assertThat("BY_CURRENCY").isEqualTo(reportResponseDto.getName());
        assertThat(startDate).isEqualTo(reportResponseDto.getStartDate());
        assertThat(endDate).isEqualTo(reportResponseDto.getEndDate());

        List<SalesReportByCurrencyEntryDto> entries =
                reportResponseDto.getEntries();
        assertThat(1).isEqualTo(entries.size());

        SalesReportByCurrencyEntryDto usdEntry = entries.get(0);
        assertThat("USD").isEqualTo(usdEntry.getCurrency());
        assertThat(2).isEqualTo(usdEntry.getNumberOfPurchases());
        assertThat(300.0).isEqualTo(usdEntry.getTotalRevenue());
        assertThat(100.0).isEqualTo(usdEntry.getTotalDiscount());
        assertThat(200.0).isEqualTo(usdEntry.getTotalNetRevenue());

        verify(purchaseService, times(1))
                .findPurchasesGroupedByCurrency();
    }

    @Test
    public void shouldGenerateSalesByCurrencyReportOnlyForPurchasesWithinReportDatesRangeIncludingBoundaryDates() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(10);

        Purchase purchaseUSD1 = new Purchase(
                123L,
                "NikeShoes",
                LocalDate.now().minusDays(40),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD2 = new Purchase(
                133L,
                "NikeShoes",
                LocalDate.now().minusDays(30),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD3 = new Purchase(
                143L,
                "NikeShoes",
                LocalDate.now().minusDays(10),
                150.0,
                50.0,
                "USD"
        );

        Purchase purchaseUSD4 = new Purchase(
                153L,
                "NikeShoes",
                LocalDate.now().minusDays(5),
                150.0,
                50.0,
                "USD"
        );

        List<Purchase> purchasesUSD = List.of(
                purchaseUSD1,
                purchaseUSD2,
                purchaseUSD3,
                purchaseUSD4
        );

        List<List<Purchase>> purchasesGroupedByCurrency = List.of(
                purchasesUSD
        );

        when(purchaseService.findPurchasesGroupedByCurrency())
                .thenReturn(purchasesGroupedByCurrency);

        SalesReportByCurrencyResponseDto reportResponseDto =
                salesReportService.generateSalesByCurrencyReport(
                        startDate,
                        endDate
                );

        assertNotNull(reportResponseDto);
        assertThat("SALES").isEqualTo(reportResponseDto.getCategory());
        assertThat("BY_CURRENCY").isEqualTo(reportResponseDto.getName());
        assertThat(startDate).isEqualTo(reportResponseDto.getStartDate());
        assertThat(endDate).isEqualTo(reportResponseDto.getEndDate());

        List<SalesReportByCurrencyEntryDto> entries =
                reportResponseDto.getEntries();
        assertThat(1).isEqualTo(entries.size());

        SalesReportByCurrencyEntryDto usdEntry = entries.get(0);
        assertThat("USD").isEqualTo(usdEntry.getCurrency());
        assertThat(2).isEqualTo(usdEntry.getNumberOfPurchases());
        assertThat(300.0).isEqualTo(usdEntry.getTotalRevenue());
        assertThat(100.0).isEqualTo(usdEntry.getTotalDiscount());
        assertThat(200.0).isEqualTo(usdEntry.getTotalNetRevenue());

        verify(purchaseService, times(1))
                .findPurchasesGroupedByCurrency();
    }

    @Test
    public void shouldThrowFailedToGenerateReportExceptionForInvalidDates() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().plusDays(30);

        assertThatThrownBy(() -> salesReportService
                .generateSalesByCurrencyReport(startDate, endDate))
                .isInstanceOf(FailedToGenerateReportException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to generate report.",
                        HttpStatus.BAD_REQUEST,
                        "Report dates can not be from the future."
                );

        verify(purchaseService, times(0))
                .findPurchasesGroupedByCurrency();
    }
}