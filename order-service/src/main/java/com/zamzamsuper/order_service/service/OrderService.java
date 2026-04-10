package com.zamzamsuper.order_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zamzamsuper.order_service.dto.OrderItemPromotionResponse;
import com.zamzamsuper.order_service.dto.OrderItemResponse;
import com.zamzamsuper.order_service.dto.OrderPromotionResponse;
import com.zamzamsuper.order_service.dto.OrderRequest;
import com.zamzamsuper.order_service.dto.OrderResponse;
import com.zamzamsuper.order_service.model.Order;
import com.zamzamsuper.order_service.model.OrderItem;
import com.zamzamsuper.order_service.model.OrderItemPromotion;
import com.zamzamsuper.order_service.model.OrderPromotion;
import com.zamzamsuper.order_service.model.Payment;
import com.zamzamsuper.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        // 1. Create the Order entity
        Order order = mapToEntity(request);

        // 2. Map Items and set bidirectional relationship
        if (request.getItems() != null) {
            List<OrderItem> items = request.getItems().stream().map(itemReq -> {
                OrderItem item = OrderItem.builder()
                        .order(order) // CRITICAL: Link to parent
                        .batchNumber(itemReq.getBatchNumber())
                        .quantity(itemReq.getQuantity())
                        .unitPrice(itemReq.getUnitPrice())
                        .taxAmount(itemReq.getTaxAmount())
                        .subtotal(itemReq.getSubtotal())
                        .discountAmount(itemReq.getDiscountAmount())
                        .build();

                // Map Item-wise Promotions
                if (itemReq.getItemPromotions() != null) {
                    List<OrderItemPromotion> itemPromos = itemReq.getItemPromotions().stream()
                            .map(pReq -> OrderItemPromotion.builder()
                                    .orderItem(item) // CRITICAL: Link to Item
                                    .promotionRuleCode(pReq.getPromotionRuleCode())
                                    .discountAmount(pReq.getDiscountAmount())
                                    .appliedQty(pReq.getAppliedQty())
                                    .override(pReq.getOverride())
                                    .metadata(pReq.getMetadata())
                                    .build())
                            .collect(Collectors.toList());
                    item.setItemPromotions(itemPromos);
                }
                return item;
            }).collect(Collectors.toList());
            order.setItems(items);
        }

        // 3. Map Order-level Promotions
        if (request.getOrderPromotions() != null) {
            order.setOrderPromotions(request.getOrderPromotions().stream().map(pReq -> 
                OrderPromotion.builder()
                    .order(order) // Link to parent
                    .promotionRuleCode(pReq.getPromotionRuleCode())
                    .discountAmount(pReq.getDiscountAmount())
                    .override(pReq.getOverride())
                    .metadata(pReq.getMetadata())
                    .build()
            ).collect(Collectors.toList()));
        }

        // 4. Map Split Payments
        if (request.getPayments() != null) {
            order.setPayments(request.getPayments().stream().map(payReq -> 
                Payment.builder()
                    .order(order) // Link to parent
                    .paymentMethod(payReq.getPaymentMethod())
                    .amountPaid(payReq.getAmountPaid())
                    .paymentStatus(payReq.getPaymentStatus())
                    .transactionReference(payReq.getTransactionReference())
                    .build()
            ).collect(Collectors.toList()));
        }

        // 5. Save everything in one transaction
        // CascadeType.ALL will save items, payments, and all promotions automatically
        Order savedOrder = orderRepository.save(order);

        // 6. TODO: Trigger an event to Inventory Service to reduce stock for the selected Batch IDs
        // 7. Map to Response (Omitting details for brevity, but same logic applies)
        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

        existingOrder.setCustomerId(request.getCustomerId());
        existingOrder.setStaffId(request.getStaffId());
        existingOrder.setStatus(request.getStatus());
        existingOrder.setPaidAmount(request.getPaidAmount());
        existingOrder.setBalanceAmount(request.getBalanceAmount());
        // Note: For complex updates involving lists, usually you clear and re-add 
        // or update specific items to avoid orphan records.

        Order updated = orderRepository.save(existingOrder);
        return mapToResponse(updated);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .staffId(order.getStaffId())
                .invoiceNumber(order.getInvoiceNumber())
                .totalAmount(order.getTotalAmount())
                .taxAmount(order.getTaxAmount())
                .grandTotal(order.getGrandTotal())
                .status(order.getStatus())
                .orderType(order.getOrderType())
                .createdAt(order.getCreatedAt())
                // 1. Map Items
                .items(order.getItems() != null ? order.getItems().stream().map(item -> 
                    OrderItemResponse.builder()
                        .id(item.getId())
                        .batchNumber(item.getBatchNumber())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .taxAmount(item.getTaxAmount())
                        .subtotal(item.getSubtotal())
                        .discountAmount(item.getDiscountAmount())
                        // 2. Map Item-Level Promotions (Nested Stream)
                        .itemPromotions(item.getItemPromotions() != null ? item.getItemPromotions().stream().map(promo -> 
                            OrderItemPromotionResponse.builder()
                                .id(promo.getId())
                                .promotionRuleCode(promo.getPromotionRuleCode())
                                .discountAmount(promo.getDiscountAmount())
                                .appliedQty(promo.getAppliedQty())
                                .override(promo.getOverride())
                                .metadata(promo.getMetadata())
                                .build()
                        ).collect(Collectors.toList()) : null)
                        .build()
                ).collect(Collectors.toList()) : null)
                 // 3. Map Order-Level Promotions
                .orderPromotions(order.getOrderPromotions() != null ? order.getOrderPromotions().stream().map(promo -> 
                    OrderPromotionResponse.builder()
                        .id(promo.getId())
                        .promotionRuleCode(promo.getPromotionRuleCode())
                        .discountAmount(promo.getDiscountAmount())
                        .build()
                ).collect(Collectors.toList()) : null)
                .build();
    }

    private Order mapToEntity(OrderRequest request) {
        return Order.builder()
                .customerId(request.getCustomerId())
                .staffId(request.getStaffId())
                .invoiceNumber(request.getInvoiceNumber())
                .totalAmount(request.getTotalAmount())
                .taxAmount(request.getTaxAmount())
                .totalItemDiscountAmount(request.getTotalItemDiscountAmount())
                .orderDiscountAmount(request.getOrderDiscountAmount())
                .grandTotal(request.getGrandTotal())
                .paidAmount(request.getPaidAmount())
                .creditAmount(request.getCreditAmount())
                .balanceAmount(request.getBalanceAmount())
                .status(request.getStatus())
                .orderType(request.getOrderType())
                .build();
    }
}