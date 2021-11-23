package com.example.cafexapplication.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal price;
    private Preparation preparation;
    private Category category;

    public MenuItem(String name, BigDecimal price, Preparation preparation, Category category) {
        this.name = name;
        this.price = price;
        this.preparation = preparation;
        this.category = category;
    }

    public MenuItem(String name, String price, Preparation preparation, Category category) {
        this.name = name;
        this.price = new BigDecimal(price);
        this.preparation = preparation;
        this.category = category;
    }
}