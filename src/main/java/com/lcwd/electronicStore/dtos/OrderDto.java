package com.lcwd.electronicStore.dtos;

import com.lcwd.electronicStore.entities.OrderItem;
import com.lcwd.electronicStore.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto {

    private String orderId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    private double orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate=new Date();

    private Date deliveredDate;

//    private User user;

    private List<OrderItemDto> orderItems = new ArrayList<>();

}
