package br.unicap.model;

import java.util.ArrayList;
import java.util.Random;

public class Cart {

    public String id = this.randomString();

    public ArrayList<Product> products = new ArrayList<>();

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
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

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", products=" + products +
                '}';
    }
}
