package br.unicap.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;

    @Transient
    private String cartId;

    @ManyToMany
    @JoinTable(
            name = "product_order",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Product> products;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "card_number")
    private Long cardNumber;

    private String address;

    public Order(Long id, String ownerName, List products, Double total_price, Long card_number, String address) {
        this.id = id;
        this.ownerName = ownerName;
        this.products = products;
        this.totalPrice = total_price;
        this.cardNumber = card_number;
        this.address = address;
    }

    public Order() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String owner_name) {
        this.ownerName = owner_name;
    }

    public List getProducts() {
        return products;
    }


    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }
}
