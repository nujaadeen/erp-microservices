package com.zamzamsuper.promotion_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zamzamsuper.promotion_service.enums.RuleType;
import com.zamzamsuper.promotion_service.enums.Scope;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PromotionRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(unique = true)
    private String ruleCode;

    @Enumerated(EnumType.STRING)
    private RuleType ruleType; 

    @Enumerated(EnumType.STRING)
    private Scope scope; 

    private BigDecimal discountValue;
    private BigDecimal minCartValue;
    private BigDecimal maxDiscount;
    private String batchNumber;
    private Boolean stackable;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @OneToOne(mappedBy = "promotionRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private BogoLogic bogoLogic;

    @OneToMany(mappedBy = "promotionRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionPaymentMethod> paymentMethods;

    @OneToMany(mappedBy = "promotionRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionCustomerGroup> customerGroups;
}