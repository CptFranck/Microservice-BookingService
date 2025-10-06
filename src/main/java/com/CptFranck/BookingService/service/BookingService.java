package com.CptFranck.BookingService.service;

import com.CptFranck.BookingService.client.InventoryServiceClient;
import com.CptFranck.BookingService.dto.BookingEvent;
import com.CptFranck.BookingService.dto.BookingRequest;
import com.CptFranck.BookingService.dto.BookingResponse;
import com.CptFranck.BookingService.dto.InventoryResponse;
import com.CptFranck.BookingService.entity.CustomerEntity;
import com.CptFranck.BookingService.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;

    private final InventoryServiceClient inventoryServiceClient;

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingService(com.CptFranck.BookingService.repository.CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient, KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request) {
        final CustomerEntity customer = customerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not Found !"));

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        log.info("Inventory Response : {}", inventoryResponse);

        if(inventoryResponse.getCapacity() < request.getTicketCount())
            throw (new RuntimeException("Not enough Tickets !"));

        final BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);
        log.info("Booking Event : {}", bookingEvent);

        kafkaTemplate.send("booking", bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(final BookingRequest request,
                                            final CustomerEntity customer,
                                            final InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }
}
