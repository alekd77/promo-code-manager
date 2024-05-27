package com.promocodes.promocodesmanager.promocode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest {
    @Mock
    private PromoCodeRepository promoCodeRepository;

    private PromoCodeService promoCodeService;

    @BeforeEach
    void setUp() {
        promoCodeService = new PromoCodeService(promoCodeRepository);
    }

    @Test
    public void shouldReturnAllPromoCodes() {
        promoCodeService.getAllPromoCodes();

        verify(promoCodeRepository, times(1))
                .findAll();
    }
}