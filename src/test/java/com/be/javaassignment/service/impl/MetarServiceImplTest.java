package com.be.javaassignment.service.impl;

import com.be.javaassignment.dto.metar.MetarRequestDto;
import com.be.javaassignment.dto.metar.MetarResponseDto;
import com.be.javaassignment.error.MetarDataNotFoundException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.MetarMapper;
import com.be.javaassignment.mapper.SubscriptionMapper;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.MetarRepository;
import com.be.javaassignment.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetarServiceImplTest {

    @Mock
    private MetarRepository metarRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionMapper subscriptionMapper;
    @Mock
    private MetarMapper metarMapper;
    @InjectMocks
    private MetarServiceImpl metarService;

    private Subscription subscription;
    private Metar metar;
    private MetarRequestDto metarRequestDto;
    private MetarResponseDto metarResponseDto;

    @BeforeEach
    void setUp() {
        subscription = new Subscription();
        subscription.setSubscriptionId(1L);
        subscription.setIcaoCode("LDZA");

        metar = new Metar();
        metarRequestDto = new MetarRequestDto(Instant.parse("2003-01-01T00:00:00Z"), "LDZA 070830Z VRB02KT CAVOK 20/13 Q1022 NOSIG");
        metarResponseDto = new MetarResponseDto(1L, Instant.parse("2003-01-01T00:00:00Z"),"LDZA 070830Z VRB02KT CAVOK 20/13 Q1022 NOSIG",1L);
    }

    @Test
    @DisplayName("Should return METAR data successfully")
    void shouldReturnMetarDataSuccessfully() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.of(subscription));
        when(metarRepository.findTopBySubscription_SubscriptionIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(metar));
        when(metarMapper.toResponseDto(metar)).thenReturn(metarResponseDto);

        MetarResponseDto result = metarService.getMetarData(subscription.getIcaoCode());

        assertNotNull(result);
        assertEquals("LDZA 070830Z VRB02KT CAVOK 20/13 Q1022 NOSIG", result.data());
        assertEquals(1L, result.metarId());
        assertEquals(Instant.parse("2003-01-01T00:00:00Z"), result.createdAt());
    }

    @Test
    @DisplayName("Should throw SubscriptionNotFoundException when trying to get METAR data")
    void shouldThrowSubscriptionNotFoundException() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.empty());

        SubscriptionNotFoundException exception = assertThrows(
                SubscriptionNotFoundException.class,
                () -> metarService.getMetarData(subscription.getIcaoCode())
        );

        assertEquals("Subscription for airport with ICAO code "+ subscription.getIcaoCode()+" not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw MetarDataNotFoundException when trying to get METAR data")
    void shouldThrowMetarDataNotFoundException() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.of(subscription));
        when(metarRepository.findTopBySubscription_SubscriptionIdOrderByCreatedAtDesc(subscription.getSubscriptionId()))
                .thenReturn(Optional.empty());

        MetarDataNotFoundException exception = assertThrows(
                MetarDataNotFoundException.class,
                () -> metarService.getMetarData("LDZA")
        );

        assertEquals("No METAR data found for airport with ICAO code "+ subscription.getIcaoCode(), exception.getMessage());
    }

    @Test
    @DisplayName("Should add METAR data successfully")
    void shouldAddMetarDataSuccessfully() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.of(subscription));
        when(metarMapper.toEntity(metarRequestDto, subscription)).thenReturn(metar);
        when(metarMapper.toResponseDto(metar)).thenReturn(metarResponseDto);

        MetarResponseDto result = metarService.addMetarData(subscription.getIcaoCode(), metarRequestDto);

        assertNotNull(result);
        assertEquals(metarRequestDto.data(), result.data());
        assertEquals(1L, result.metarId());
        verify(metarRepository, times(1)).save(metar);
    }

    @Test
    @DisplayName("Should throw SubscriptionNotFoundException when trying to save METAR data")
    void shouldThrowSubscriptionNotFoundExceptionWhenSavingMetarData() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode())).thenReturn(Optional.empty());

        SubscriptionNotFoundException exception = assertThrows(
                SubscriptionNotFoundException.class,
                () -> metarService.addMetarData(subscription.getIcaoCode(), metarRequestDto)
        );

        assertEquals("Subscription for airport with ICAO code "+subscription.getIcaoCode()+" not found.", exception.getMessage());
        verify(metarRepository, never()).save(any(Metar.class));
    }
}


