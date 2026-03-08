package com.seowon.coding.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // "order" is a reserved keyword in SQL
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String customerName;

    @NotNull
    private String customerEmail;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private LocalDateTime orderDate;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    
    private BigDecimal totalAmount;
    
    // Business logic
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotalAmount();
    }
    
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        recalculateTotalAmount();
    }
    
    public void recalculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void applyDiscount(String coupon) {
        BigDecimal shipping = getShipping();
        BigDecimal discount = getDiscount(coupon);
        setTotalAmount(getTotalAmount().add(shipping).subtract(discount));
    }
    
    public void markAsProcessing() {
        this.status = OrderStatus.PROCESSING;
    }


    public void markAsShipped() {
        this.status = OrderStatus.SHIPPED;
    }
    
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }
    
    public void markAsCancelled() {
        this.status = OrderStatus.CANCELLED;
    }
    
    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    private BigDecimal getShipping() {
        BigDecimal subtotal = getTotalAmount();
        return subtotal.compareTo(new BigDecimal("100.00")) >= 0 ? BigDecimal.ZERO : new BigDecimal("5.00");
    }

    private BigDecimal getDiscount(String couponCode) {
        return (couponCode != null && couponCode.startsWith("SALE")) ? new BigDecimal("10.00") : BigDecimal.ZERO;
    }

}