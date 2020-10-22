package br.unicap.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @Column(name = "amount_available")
    private Long amountAvailable;

    @Column(name = "amount_in_cart")
    private Long amountInCarts;

    @Column(name = "amount_ordered")
    private Long amountOrdered;

    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Long amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Long getAmountInCarts() {
        return amountInCarts;
    }

    public void setAmountInCarts(Long amountInCarts) {
        this.amountInCarts = amountInCarts;
    }

    public Long getAmountOrdered() {
        return amountOrdered;
    }

    public void setAmountOrdered(Long amountOrdered) {
        this.amountOrdered = amountOrdered;
    }
}
