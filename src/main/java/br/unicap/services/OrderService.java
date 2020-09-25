package br.unicap.services;

import br.unicap.model.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class OrderService extends BaseService<Order>{

    public OrderService() {
        super(Order.class);
    }
}
