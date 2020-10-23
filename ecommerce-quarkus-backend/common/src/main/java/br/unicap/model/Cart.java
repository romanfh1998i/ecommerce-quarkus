package br.unicap.model;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "tb_order")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_id")
    private String cartId = this.randomString();

    @Column(name = "owner_name")
    private String ownerName;

    @ManyToMany
    @JoinTable(
            name = "product_order",
            joinColumns = @JoinColumn(name = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "id_product")
    )
    public List<Product> products;

    @Column(name = "card_number")
    private Long cardNumber;

    private String address;

    public List<Product> getProducts() {
        return products;
    }

    private String randomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public String getCartId() {
        return cartId;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "id='" + cartId + '\'' +
                ", products=" + products +
                '}';
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
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

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }


}
