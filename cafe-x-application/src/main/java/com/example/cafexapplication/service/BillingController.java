package com.example.cafexapplication.service;


import com.example.cafexapplication.config.ServiceConfiguration;
import com.example.cafexapplication.model.MenuItem;
import com.example.cafexapplication.repository.InvoiceRepository;
import com.example.cafexapplication.repository.MenuItemRepository;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
    The thinking here is that the order would be submitted.
 */

@AllArgsConstructor
@RestController
public class BillingController {


    private BillingService service;
    private MenuItemRepository itemRepository;
    private InvoiceRepository invoiceRepository;
    private ServiceConfiguration configuration;


    @GetMapping("/order")
    public String submitOrder() {

        /*
            This is our pretend order.
            I've only got one service in this example.
            Typically an order would be created here by an Order service.

            Some systems will build payment info at checkout, some (older) systems do it after. Typically, the pattern
            is like a "router on a stick" where we'd call a utility service to perform the calculations and taxes.
            (May or may not be part of the payment service)
         */
        List<MenuItem> order = itemRepository.findAll();
        service = new BillingService(order, configuration, invoiceRepository);

        /*
            We wouldn't do this directly.
            We'd more than likely send the order off for payment after creating the invoice and maybe update an FE
            w "Order submitted status".

            - Again, calculations may or may not be performed by a payment service.

            The payment would then go off to a payment processor (like Stripe) to validate whatever method was
            provided.

            eventually it would come back with an id (or the entire payload) that would be handed back to the order s
            service, confirming the result of payment validation.

            The experience from this point would vary.
            - we'd store the final invoice for auditing purposes, but how it's displayed for customers depends.
            If we have the concept of a registered user, then past orders/invoices would be something available to
            their login account. If they don't have that concept, then we'd just dump it to the screen on
            success/rejection.

         */
        return service.createInvoice().toString();
    }

}
