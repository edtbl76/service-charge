package com.example.cafexapplication.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToMany
    @ToString.Exclude
    private List<MenuItem> purchasedItems;

    private BigDecimal subtotal;
    private BigDecimal surchargeRate;
    private BigDecimal surchargeAmount;
    private BigDecimal total;

    /*
        NOTE:
        - This is just a simple way of storing what is on the bill. We want to transparently display
            - itemization
            - subtotal
            - what the surcharge was
            - the resulting total

        Calculations are performed in the biz capabilities.

     */
    public Invoice(
            List<MenuItem> purchasedItems, BigDecimal subtotal,
            BigDecimal surchargeRate, BigDecimal surchargeAmount, BigDecimal total) {
        this.purchasedItems = purchasedItems;
        this.subtotal = subtotal;
        this.surchargeRate = surchargeRate;
        this.surchargeAmount = surchargeAmount;
        this.total = total;
    }
}