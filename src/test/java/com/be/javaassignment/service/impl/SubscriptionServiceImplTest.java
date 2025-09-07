package com.be.javaassignment.service.impl;

import com.be.javaassignment.dto.subscription.SubscriptionFilterDto;
import com.be.javaassignment.dto.subscription.SubscriptionRequestDto;
import com.be.javaassignment.dto.subscription.SubscriptionResponseDto;
import com.be.javaassignment.error.InvalidIcaoCodeFormatException;
import com.be.javaassignment.error.SubscriptionAlreadyExistsException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.SubscriptionMapper;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionServiceImpl Tests")
class SubscriptionServiceImplTest {

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionMapper subscriptionMapper;


    private Subscription subscription;
    private SubscriptionRequestDto subscriptionRequestDto;
    private SubscriptionResponseDto subscriptionResponseDto;

    @BeforeEach
    void setUp(){
        this.subscription= new Subscription();
        subscription.setIcaoCode("LDZA");
        subscription.setActive(true);
        this.subscriptionRequestDto =new SubscriptionRequestDto("LDZA");
        this.subscriptionResponseDto = new SubscriptionResponseDto(1L, "LDZA", true);
    }

    @Test
    @DisplayName("Should add a new subscription successfully")
    void shouldAddSubscriptionSuccessfully() {
        when(subscriptionRepository.findByIcaoCode(subscriptionRequestDto.icaoCode())).thenReturn(Optional.empty());
        when(subscriptionMapper.toDto(any(Subscription.class))).thenReturn(subscriptionResponseDto);

        SubscriptionResponseDto result = subscriptionService.addSubscription(subscriptionRequestDto);

        assertNotNull(result);
        assertEquals(subscriptionRequestDto.icaoCode(), result.icaoCode());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionRepository).save(argThat(sub -> sub.getIcaoCode().equals(subscriptionRequestDto.icaoCode())));
    }

    @Test
    @DisplayName("Should throw SubscriptionAlreadyExistsException when subscription already exists")
    void shouldThrowSubscriptionAlreadyExistsException() {
        when(subscriptionRepository.findByIcaoCode(subscriptionRequestDto.icaoCode())).thenReturn(Optional.of(subscription));

        SubscriptionAlreadyExistsException exception=assertThrows(SubscriptionAlreadyExistsException.class,()->subscriptionService.addSubscription(subscriptionRequestDto));
        assertEquals("Subscription for airport with ICAO code "+ subscriptionRequestDto.icaoCode() + " already exists.", exception.getMessage());
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Should throw InvalidIcaoCodeFormatException for invalid ICAO code")
    void shouldThrowInvalidIcaoCodeFormatException() {
        SubscriptionRequestDto invalidDto = new SubscriptionRequestDto("1234");

        InvalidIcaoCodeFormatException exception = assertThrows(
                InvalidIcaoCodeFormatException.class,
                () -> subscriptionService.addSubscription(invalidDto)
        );

        assertEquals("Invalid ICAO code format. ICAO code must contain exactly 4 letters: " + invalidDto.icaoCode(), exception.getMessage());
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Should delete subscription successfully")
    void shouldDeleteSubscriptionSuccessfully() {
        when(subscriptionRepository.findByIcaoCode(subscription.getIcaoCode())).thenReturn(Optional.of(subscription));
        when(subscriptionMapper.toDto(subscription)).thenReturn(subscriptionResponseDto);

        SubscriptionResponseDto result = subscriptionService.deleteSubscription(subscriptionRequestDto.icaoCode());

        assertEquals(subscriptionRequestDto.icaoCode(), result.icaoCode());
        assertEquals(1L, result.subscriptionId());
        verify(subscriptionRepository, times(1)).delete(subscription);
    }

    @Test
    @DisplayName("Should throw SubscriptionNotFoundException when subscription does not exist")
    void shouldThrowSubscriptionNotFoundException() {
        when(subscriptionRepository.findByIcaoCode(subscriptionRequestDto.icaoCode())).thenReturn(Optional.empty());

        SubscriptionNotFoundException exception=assertThrows(SubscriptionNotFoundException.class,()->subscriptionService.deleteSubscription(subscriptionRequestDto.icaoCode()));

        assertEquals("Subscription not found for airport with ICAO code " + subscriptionRequestDto.icaoCode(), exception.getMessage());
        verify(subscriptionRepository, never()).delete(any(Subscription.class));
    }

    @Test
    @DisplayName("Should return a list of subscriptions")
    void shouldReturnSubscriptions() {
        when(subscriptionRepository.findAll()).thenReturn(List.of(subscription));
        when(subscriptionMapper.toDto(subscription)).thenReturn(subscriptionResponseDto);


        List<SubscriptionResponseDto> result = subscriptionService.getSubscriptions(new SubscriptionFilterDto(true, null));

        assertEquals(1, result.size());
        assertEquals(subscriptionRequestDto.icaoCode(), result.getFirst().icaoCode());
        assertEquals(1L, result.getFirst().subscriptionId());
    }
}

