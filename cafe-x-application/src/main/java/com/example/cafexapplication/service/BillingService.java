package com.example.cafexapplication.service;


import com.example.cafexapplication.config.ServiceConfiguration;
import com.example.cafexapplication.model.Invoice;
import com.example.cafexapplication.model.MenuItem;
import com.example.cafexapplication.repository.InvoiceRepository;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.List;

import static com.example.cafexapplication.model.Category.FOOD;
import static com.example.cafexapplication.model.Preparation.HOT;


@Service
public class BillingService {

    private static final BigDecimal DEFAULT_FOOD_SURCHARGE = new BigDecimal(".1");
    private static final BigDecimal DEFAULT_HOT_FOOD_SURCHARGE = new BigDecimal(".2");


    private final List<MenuItem> purchasedItems;
    private final ServiceConfiguration configuration;
    private final CurrencyUnit currencyUnit;
    private final InvoiceRepository repository;


    private Money surchargeRate;
    private Money surchargeAmount;
    private Money subtotal;
    private Money finalTotal;

    public BillingService(List<MenuItem> purchasedItems, ServiceConfiguration configuration, InvoiceRepository repository) {
        this.purchasedItems = purchasedItems;
        this.configuration = configuration;
        this.currencyUnit = Monetary.getCurrency(configuration.getCurrency());
        this.repository = repository;
    }


    private void calculateSubtotal() {
        this.subtotal = purchasedItems.stream()
                .map(menuItem -> Money.of(menuItem.getPrice(), currencyUnit))
                .reduce(Money.of(0, currencyUnit), Money::add);
    }

    public Invoice createInvoice() {

        /*
            I did this deliberately to demonstrate where there may be state changes in an EDA, data pipelining, or
            potentially the use of a business rules system (BPMN, etc.)
         */
        calculateSubtotal();
        calculateSurcharges();
        calculateTotal();

        /*
            When I construct the Invoice I want to convert from the JSR 354 compliant Money values back to the
            BigDecimal values. (These are easier to use w/ JPA)
         */
        Invoice invoice = new Invoice(this.purchasedItems,
                this.subtotal.getNumberStripped(),
                this.surchargeRate.getNumberStripped(),
                this.surchargeAmount.getNumberStripped(),
                this.finalTotal.getNumberStripped());

        repository.save(invoice);
        return invoice;
    }


    /*
        Deliberately split out some of these functions to demonstrate some of the separation in the business logic.

        While they are very simple right now, it isn't uncommon to have accounting flows that split these out.
        - in those situations, rather than dealing w/ values that are provided to the service, each function takes
        arguments from the caller and returns the information.

     */
    private void calculateSurcharges() {


        if (purchasedItems.stream().anyMatch(menuItem -> menuItem.getCategory().equals(FOOD))) {

            if (purchasedItems.stream().anyMatch(menuItem -> menuItem.getPreparation().equals(HOT))) {
                this.surchargeRate = Money.of(DEFAULT_HOT_FOOD_SURCHARGE, currencyUnit);
                Money hot_surcharge = subtotal.multiply(this.surchargeRate.getNumberStripped());
                Money max_hot_surcharge = Money.of(20, currencyUnit);
                this.surchargeAmount = hot_surcharge.isLessThan(max_hot_surcharge) ? hot_surcharge : max_hot_surcharge;

            } else {
                this.surchargeRate = Money.of(DEFAULT_FOOD_SURCHARGE, currencyUnit);
                this.surchargeAmount = subtotal.multiply(this.surchargeRate.getNumberStripped());
            }

        } else {
            this.surchargeRate = Money.of(0, currencyUnit);
            this.surchargeAmount = Money.of(0, currencyUnit);
        }

    }

    private void calculateTotal() {

        this.finalTotal = this.subtotal.add(this.surchargeAmount);

    }





}
