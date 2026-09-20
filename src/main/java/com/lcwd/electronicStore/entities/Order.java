package com.lcwd.electronicStore.entities;


import jakarta.persistence.*;
import lombok.*;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

//    PENDING, DISPATCHED, DELIVERED
//    enum
    private String orderStatus;

//    NOT-PAID, PAID
//    enum
//    BOOLEAN - false-NOT-PAID, true-PAID
    private String paymentStatus;

    private double orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

}
