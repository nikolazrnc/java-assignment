package com.be.javaassignment.service;

import com.be.javaassignment.dto.SubscriptionDto;
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


    private SubscriptionDto subscriptionDto;

    @BeforeEach
    void setUp(){
        this.subscriptionDto=new SubscriptionDto("LDZA");
    }

    @Test
    @DisplayName("Should add a new subscription successfully")
    void shouldAddSubscriptionSuccessfully() {
        Subscription newSubscription=new Subscription();
        newSubscription.setIcaoCode(subscriptionDto.icaoCode());

        when(subscriptionRepository.findByIcaoCode(subscriptionDto.icaoCode())).thenReturn(Optional.empty());
        when(subscriptionMapper.toDto(any(Subscription.class))).thenReturn(subscriptionDto);

        SubscriptionDto result = subscriptionService.addSubscription(subscriptionDto);

        assertNotNull(result);
        assertEquals(subscriptionDto.icaoCode(), result.icaoCode());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionRepository).save(argThat(sub -> sub.getIcaoCode().equals(subscriptionDto.icaoCode())));
    }

    @Test
    @DisplayName("Should throw SubscriptionAlreadyExistsException when subscription already exists")
    void shouldThrowSubscriptionAlreadyExistsException() {
        Subscription newSubscription=new Subscription();
        newSubscription.setIcaoCode(subscriptionDto.icaoCode());

        when(subscriptionRepository.findByIcaoCode(subscriptionDto.icaoCode())).thenReturn(Optional.of(newSubscription));

        SubscriptionAlreadyExistsException exception=assertThrows(SubscriptionAlreadyExistsException.class,()->subscriptionService.addSubscription(subscriptionDto));

        assertEquals("Subscription for airport "+ subscriptionDto.icaoCode() + " already exists.", exception.getMessage());
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void shouldDeleteSubscriptionSuccessfully() {
        Subscription toDelete = new Subscription();
        toDelete.setIcaoCode(subscriptionDto.icaoCode());
        toDelete.setSubscriptionId(1L);

        when(subscriptionRepository.findByIcaoCode(toDelete.getIcaoCode())).thenReturn(Optional.of(toDelete));
        when(subscriptionMapper.toDto(toDelete)).thenReturn(new SubscriptionDto(toDelete.getIcaoCode()));

        SubscriptionDto result = subscriptionService.deleteSubscription(subscriptionDto.icaoCode());

        assertEquals(subscriptionDto.icaoCode(), result.icaoCode());
        verify(subscriptionRepository, times(1)).delete(toDelete);
    }

    @Test
    @DisplayName("Should throw SubscriptionNotFoundException when subscription does not exist")
    void shouldThrowSubscriptionNotFoundException() {
        Subscription toDelete=new Subscription();
        toDelete.setIcaoCode(subscriptionDto.icaoCode());

        when(subscriptionRepository.findByIcaoCode(subscriptionDto.icaoCode())).thenReturn(Optional.empty());

        SubscriptionNotFoundException exception=assertThrows(SubscriptionNotFoundException.class,()->subscriptionService.deleteSubscription(subscriptionDto.icaoCode()));

        assertEquals("Subscription not found for: " + subscriptionDto.icaoCode(), exception.getMessage());
        verify(subscriptionRepository, never()).delete(any(Subscription.class));
    }

    @Test
    @DisplayName("Should return a list of subscriptions")
    void shouldReturnSubscriptions() {
        Subscription sub = new Subscription();
        sub.setIcaoCode(subscriptionDto.icaoCode());

        when(subscriptionRepository.findAll()).thenReturn(List.of(sub));
        when(subscriptionMapper.toDto(sub)).thenReturn(new SubscriptionDto(sub.getIcaoCode()));

        List<SubscriptionDto> result = subscriptionService.getSubscriptions();

        assertEquals(1, result.size());
        assertEquals(subscriptionDto.icaoCode(), result.get(0).icaoCode());
    }
}