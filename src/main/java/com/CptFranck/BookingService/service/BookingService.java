package com.CptFranck.BookingService.service;

import com.CptFranck.BookingService.client.InventoryServiceClient;
import com.CptFranck.BookingService.dto.BookingRequest;
import com.CptFranck.BookingService.dto.BookingResponse;
import com.CptFranck.BookingService.dto.InventoryResponse;
import com.CptFranck.BookingService.entity.CustomerEntity;
import com.CptFranck.BookingService.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final CustomerRepository customerRepository;

    private final InventoryServiceClient inventoryServiceClient;

    public BookingService(com.CptFranck.BookingService.repository.CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public BookingResponse createBooking(final BookingRequest request) {
        final CustomerEntity customer = customerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not Found !"));

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        System.out.println("Inventory Service Response" + inventoryResponse);

        if(inventoryResponse.getCapacity() < request.getTicketCount())
            throw (new RuntimeException("Not enough Tickets !"));


        return BookingResponse.builder().build();
    }
}
