package com.zamzamsuper.order_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zamzamsuper.order_service.dto.SalesReturnItemResponse;
import com.zamzamsuper.order_service.dto.SalesReturnItemReviewRequest;
import com.zamzamsuper.order_service.dto.SalesReturnItemReviewResponse;
import com.zamzamsuper.order_service.dto.SalesReturnRequest;
import com.zamzamsuper.order_service.dto.SalesReturnResponse;
import com.zamzamsuper.order_service.dto.SalesReturnReviewRequest;
import com.zamzamsuper.order_service.dto.SalesReturnReviewResponse;
import com.zamzamsuper.order_service.enums.ReturnStatus;
import com.zamzamsuper.order_service.model.Order;
import com.zamzamsuper.order_service.model.OrderItem;
import com.zamzamsuper.order_service.model.SalesReturn;
import com.zamzamsuper.order_service.model.SalesReturnItem;
import com.zamzamsuper.order_service.repository.OrderItemRepository;
import com.zamzamsuper.order_service.repository.OrderRepository;
import com.zamzamsuper.order_service.repository.SalesReturnRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesReturnService {

    private final SalesReturnRepository returnRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

//     @Transactional
//     public SalesReturnResponse createReturn(SalesReturnRequest request) {
//         Order order = orderRepository.findById(request.getOrderId())
//                 .orElseThrow(() -> new EntityNotFoundException("Order not found"));

//         SalesReturn salesReturn = SalesReturn.builder()
//                 .order(order)
//                 .returnDate(LocalDateTime.now())
//                 .refundMethod(request.getRefundMethod())
//                 .totalRefundAmount(request.getTotalRefundAmount())
//                 .returnStatus(request.getReturnStatus())
//                 .approvedById(request.getApprovedById())
//                 .build();

//         // Map items
//         List<SalesReturnItem> items = request.getItems().stream()
//                 .map(itemReq -> mapItemRequestToEntity(itemReq, salesReturn))
//                 .collect(Collectors.toList());

//         salesReturn.setItems(items);
//         return mapToResponse(returnRepository.save(salesReturn));
//     }

    @Transactional
    public SalesReturnResponse createReturn(SalesReturnRequest request) {
        // 1. Initialize Return Header
        SalesReturn salesReturn = SalesReturn.builder()
                .returnDate(LocalDateTime.now())
                .refundMethod(request.getRefundMethod())
                .totalRefundAmount(request.getTotalRefundAmount())
                .returnStatus(request.getReturnStatus())
                .approvedById(request.getApprovedById())
                .build();

        // 2. Link Order if it exists (Flow 1)
        if (request.getOrderId() != null) {
                Order order = orderRepository.findById(request.getOrderId())
                        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
                salesReturn.setOrder(order);
        }

        // 3. Map Items with Conditional Logic
        List<SalesReturnItem> items = request.getItems().stream().map(itemReq -> {
                SalesReturnItem item = SalesReturnItem.builder()
                        .salesReturn(salesReturn)
                        .batchNumber(itemReq.getBatchNumber())
                        .qtyReturned(itemReq.getQtyReturned())
                        .itemCondition(itemReq.getItemCondition())
                        .refundSubTotal(itemReq.getRefundSubTotal())
                        .manualPrice(itemReq.getManualPrice())
                        .reason(itemReq.getReason())
                        .build();

                if (request.getOrderId() != null && itemReq.getOrderItemId() != null) {
                // FLOW 1: With Invoice
                OrderItem orderItem = orderItemRepository.findById(itemReq.getOrderItemId())
                        .orElseThrow(() -> new EntityNotFoundException("Original sale item not found"));
                
                item.setOrderItem(orderItem);
                
                // Calculate Net Price: (Subtotal - Discount) / TotalQty
                BigDecimal totalDiscount = orderItem.getDiscountAmount() != null ? orderItem.getDiscountAmount() : BigDecimal.ZERO;
                BigDecimal netTotal = orderItem.getSubtotal().subtract(totalDiscount);
                BigDecimal calculatedRefundPrice = netTotal.divide(BigDecimal.valueOf(orderItem.getQuantity()), 2, RoundingMode.HALF_UP);
                
                item.setRefundUnitPrice(calculatedRefundPrice);
                } else {
                // FLOW 2: Without Invoice
                // Supervisor manually provides the price in the request
                item.setRefundUnitPrice(itemReq.getRefundUnitPrice());
                }
                
                return item;
        }).collect(Collectors.toList());

        salesReturn.setItems(items);
        return mapToResponse(returnRepository.save(salesReturn));
        }

    // @Transactional
    // public SalesReturnReviewResponse calculateRefundPreview(SalesReturnReviewRequest request) {
    //     BigDecimal totalRefund = BigDecimal.ZERO;
    //     List<SalesReturnItemReviewResponse> itemResponses = new ArrayList<>();

    //     System.out.println("Calculating refund preview for invoice: " + request.getInvoiceNumber());
    //     for (SalesReturnItemReviewRequest itemReq : request.getItems()) {
    //         BigDecimal refundPrice;

    //         // CASE 1: Invoice Exists - Calculate based on historical sale price
    //         if (request.getInvoiceNumber() != null && !request.getInvoiceNumber().isEmpty()) {
    //             Optional<OrderItem> originalItemOpt = orderItemRepository.findByInvoiceAndBatch(
    //                 request.getInvoiceNumber(), itemReq.getBatchNumber());

    //             if (originalItemOpt.isPresent()) {
    //                 OrderItem oi = originalItemOpt.get();
    //                 if (oi.getQuantity() < itemReq.getQtyReturned()) {
    //                     throw new IllegalArgumentException("Return quantity cannot exceed original sold quantity for batch " + itemReq.getBatchNumber());
    //                 }
    //                 // Formula: (Subtotal - Discount) / Qty
    //                 BigDecimal netAmount = oi.getSubtotal().subtract(
    //                     oi.getDiscountAmount() != null ? oi.getDiscountAmount() : BigDecimal.ZERO
    //                 );
    //                 refundPrice = netAmount.divide(BigDecimal.valueOf(oi.getQuantity()), 2, RoundingMode.HALF_UP);
    //             } else {
    //                 // If invoice exists but this batch wasn't on it, you might want to throw an error 
    //                 // or fall back to manual. For now, let's assume it requires manual supervisor price.
    //                 // refundPrice = itemReq.getRefundUnitPrice() != null ? itemReq.getRefundUnitPrice() : BigDecimal.ZERO;
    //                 throw new EntityNotFoundException("Batch " + itemReq.getBatchNumber() + " not found on invoice " + request.getInvoiceNumber());
    //             }
    //         } 
    //         // CASE 2: No Invoice - Use Supervisor/Manual Price provided in request
    //         else {
    //             refundPrice = itemReq.getRefundUnitPrice() != null ? itemReq.getRefundUnitPrice() : BigDecimal.ZERO;
    //             // Note: You could also fetch the current selling price of the batch here if refundPrice is null
    //         }

    //         BigDecimal itemTotal = refundPrice.multiply(BigDecimal.valueOf(itemReq.getQtyReturned()));
    //         totalRefund = totalRefund.add(itemTotal);

    //         itemResponses.add(SalesReturnItemReviewResponse.builder()
    //             .batchNumber(itemReq.getBatchNumber())
    //             .qtyReturned(itemReq.getQtyReturned())
    //             .refundUnitPrice(refundPrice) // BE populated or confirmed this
    //             .itemSubtotal(itemTotal)
    //             .itemCondition(itemReq.getItemCondition())
    //             .reason(itemReq.getReason())
    //             .build());
    //     }

    //     return SalesReturnReviewResponse.builder()
    //         .invoiceNumber(request.getInvoiceNumber())
    //         .totalRefundAmount(totalRefund)
    //         .items(itemResponses)
    //         .returnStatus(ReturnStatus.REQUESTED) // Just for preview
    //         .build();
    //     }

    @Transactional
    public SalesReturnReviewResponse calculateRefundPreview(SalesReturnReviewRequest request) {
        BigDecimal totalRefund = BigDecimal.ZERO;
        List<SalesReturnItemReviewResponse> itemResponses = new ArrayList<>();
        Long orderId = null;

        // Fetch the Order once if invoice is provided
        Order originalOrder = null;
        if (request.getInvoiceNumber() != null && !request.getInvoiceNumber().isEmpty()) {
            originalOrder = orderRepository.findByInvoiceNumber(request.getInvoiceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Invoice " + request.getInvoiceNumber() + " not found"));
            orderId = originalOrder.getId();
        }

        for (SalesReturnItemReviewRequest itemReq : request.getItems()) {
            BigDecimal refundPrice = BigDecimal.ZERO;
            Long orderItemId = null;

            if (originalOrder != null) {
                // CASE 1: Find the item within the Order's collection in memory
                OrderItem oi = originalOrder.getItems().stream()
                    .filter(i -> i.getBatchNumber().equals(itemReq.getBatchNumber()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(
                        "Batch " + itemReq.getBatchNumber() + " not found in invoice " + request.getInvoiceNumber()));
                orderItemId = oi.getId();
                // Validation: Return Qty vs Sold Qty
                if (oi.getQuantity() < itemReq.getQtyReturned()) {
                    throw new IllegalArgumentException("Cannot return " + itemReq.getQtyReturned() + 
                        " units. Only " + oi.getQuantity() + " were sold for batch " + itemReq.getBatchNumber());
                }

                // Calculation
                BigDecimal netAmount = oi.getSubtotal().subtract(
                    oi.getDiscountAmount() != null ? oi.getDiscountAmount() : BigDecimal.ZERO
                );
                refundPrice = netAmount.divide(BigDecimal.valueOf(oi.getQuantity()), 2, RoundingMode.HALF_UP);
            } else {
                // CASE 2: No Invoice - Use provided manual price
                refundPrice = itemReq.getRefundUnitPrice() != null ? itemReq.getRefundUnitPrice() : BigDecimal.ZERO;
            }

            BigDecimal itemTotal = refundPrice.multiply(BigDecimal.valueOf(itemReq.getQtyReturned()));
            totalRefund = totalRefund.add(itemTotal);

            itemResponses.add(SalesReturnItemReviewResponse.builder()
                .orderItemId(orderItemId)
                .batchNumber(itemReq.getBatchNumber())
                .qtyReturned(itemReq.getQtyReturned())
                .refundUnitPrice(refundPrice)
                .refundSubTotal(itemTotal)
                .itemCondition(itemReq.getItemCondition())
                .manualPrice(Boolean.TRUE.equals(itemReq.getManualPrice()))
                .reason(itemReq.getReason())
                .build());
        }

        return SalesReturnReviewResponse.builder()
            .orderId(orderId) // Now included in response
            .invoiceNumber(request.getInvoiceNumber())
            .totalRefundAmount(totalRefund)
            .returnStatus(ReturnStatus.REQUESTED)
            .items(itemResponses)
            .build();
    }

    public List<SalesReturnResponse> getAllSalesReturns() {
        return returnRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SalesReturnResponse getSalesReturnById(Long id) {
        SalesReturn salesReturn = returnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales return not found with id " + id));
        return mapToResponse(salesReturn);
    }
    
    public void deleteSalesReturn(Long id) {
        if (!returnRepository.existsById(id)) {
            throw new RuntimeException("Sales return not found with id " + id);
        }
        returnRepository.deleteById(id);
    }

    // @Transactional
    // public SalesReturnResponse updateReturn(Long id, SalesReturnRequest request) {
    //     SalesReturn existingReturn = returnRepository.findById(id)
    //             .orElseThrow(() -> new EntityNotFoundException("Return not found"));

    //     // Update top-level fields
    //     existingReturn.setRefundMethod(request.getRefundMethod());
    //     existingReturn.setTotalRefundAmount(request.getTotalRefundAmount());
    //     existingReturn.setReturnStatus(request.getReturnStatus());

    //     // Update Items: Clear and Re-add or "Merge"
    //     // Method A: Clear and Re-add (Simplest, but generates new IDs)
    //     existingReturn.getItems().clear(); 
        
    //     List<SalesReturnItem> updatedItems = request.getItems().stream()
    //             .map(itemReq -> mapItemRequestToEntity(itemReq, existingReturn))
    //             .collect(Collectors.toList());
        
    //     existingReturn.getItems().addAll(updatedItems);

    //     return mapToResponse(returnRepository.save(existingReturn));
    // }

    // private SalesReturnItem mapItemRequestToEntity(SalesReturnItemRequest itemReq, SalesReturn parent) {
    //     OrderItem orderItem = orderItemRepository.findById(itemReq.getOrderItemId())
    //             .orElseThrow(() -> new EntityNotFoundException("Order Item not found"));

    //     return SalesReturnItem.builder()
    //             .salesReturn(parent)
    //             .orderItem(orderItem)
    //             .batchNumber(itemReq.getBatchNumber())
    //             .qtyReturned(itemReq.getQtyReturned())
    //             .refundUnitPrice(itemReq.getRefundUnitPrice())
    //             .itemCondition(itemReq.getItemCondition())
    //             .reason(itemReq.getReason())
    //             .build();
    // }

    private SalesReturnResponse mapToResponse(SalesReturn salesReturn) {
        if (salesReturn == null) return null;

        return SalesReturnResponse.builder()
                .id(salesReturn.getId())
                // Safely get order details
                .orderId(salesReturn.getOrder() != null ? salesReturn.getOrder().getId() : null)
                .invoiceNumber(salesReturn.getOrder() != null ? salesReturn.getOrder().getInvoiceNumber() : null)
                .returnDate(salesReturn.getReturnDate())
                .refundMethod(salesReturn.getRefundMethod())
                .totalRefundAmount(salesReturn.getTotalRefundAmount())
                .returnStatus(salesReturn.getReturnStatus())
                .approvedById(salesReturn.getApprovedById())
                .createdAt(salesReturn.getCreatedAt())
                // Map items using helper method
                .items(salesReturn.getItems() != null ? 
                        salesReturn.getItems().stream()
                        .map(this::mapToReturnItemResponse)
                        .collect(Collectors.toList()) : null)
                .build();
        }

    private SalesReturnItemResponse mapToReturnItemResponse(SalesReturnItem item) {
        return SalesReturnItemResponse.builder()
                .id(item.getId())
                .orderItemId(item.getOrderItem() != null ? item.getOrderItem().getId() : null)
                .batchNumber(item.getBatchNumber())
                .qtyReturned(item.getQtyReturned())
                .refundUnitPrice(item.getRefundUnitPrice())
                .refundSubTotal(item.getRefundSubTotal())
                .manualPrice(item.getManualPrice())
                .itemCondition(item.getItemCondition())
                .reason(item.getReason())
                .build();
        }
}
