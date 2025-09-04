package com.be.javaassignment.service;

import com.be.javaassignment.dto.MetarDto;
import com.be.javaassignment.error.MetarDataNotFoundException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.MetarMapper;
import com.be.javaassignment.mapper.SubscriptionMapper;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.MetarRepository;
import com.be.javaassignment.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private MetarDto metarData;

    @BeforeEach
    void setUp() {
        subscription = new Subscription();
        subscription.setSubscriptionId(1L);
        subscription.setIcaoCode("LDZA");

        metar = new Metar();
        metarData = new MetarDto(Instant.parse("2003-01-01T00:00:00Z"), "Sample data", "LDZA");
    }

    @Test
    void shouldReturnMetarDataSuccessfully() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.of(subscription));
        when(metarRepository.findLatestBySubscriptionId(1L))
                .thenReturn(Optional.of(metar));
        when(metarMapper.toDto(metar)).thenReturn(metarData);

        MetarDto result = metarService.getMetarData("LDZA");

        assertNotNull(result);
        assertEquals("Sample data", result.data());
        assertEquals("LDZA", result.icaoCode());
        assertEquals(Instant.parse("2003-01-01T00:00:00Z"), result.createdAt());
    }

    @Test
    void shouldThrowSubscriptionNotFoundException() {
        when(subscriptionRepository.findByIcaoCode("LDZA"))
                .thenReturn(Optional.empty());

        SubscriptionNotFoundException exception = assertThrows(
                SubscriptionNotFoundException.class,
                () -> metarService.getMetarData("LDZA")
        );

        assertEquals("Subscription for code: LDZA not found.", exception.getMessage());
    }

    @Test
    void shouldThrowMetarDataNotFoundException() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.of(subscription));
        when(metarRepository.findLatestBySubscriptionId(1L))
                .thenReturn(Optional.empty());

        MetarDataNotFoundException exception = assertThrows(
                MetarDataNotFoundException.class,
                () -> metarService.getMetarData("LDZA")
        );

        assertEquals("No METAR data found for airport: LDZA", exception.getMessage());
    }

    @Test
    void shouldAddMetarDataSuccessfully() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode()))
                .thenReturn(Optional.of(subscription));
        when(metarMapper.toEntity(metarData, subscription)).thenReturn(metar);
        when(metarMapper.toDto(metar)).thenReturn(metarData);

        MetarDto result = metarService.addMetarData("LDZA", metarData);

        assertNotNull(result);
        assertEquals("Sample data", result.data());
        assertEquals("LDZA", result.icaoCode());
        verify(metarRepository, times(1)).save(metar);
    }

    @Test
    void shouldThrowSubscriptionNotFoundExceptionWhenSavingMetarData() {
        when(subscriptionRepository.findByIcaoCode("LDZA")).thenReturn(Optional.empty());

        SubscriptionNotFoundException exception = assertThrows(
                SubscriptionNotFoundException.class,
                () -> metarService.addMetarData("LDZA", metarData)
        );

        assertEquals("Subscription for code: LDZA not found.", exception.getMessage());
        verify(metarRepository, never()).save(any(Metar.class));
    }
}
