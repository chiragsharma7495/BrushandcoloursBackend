package com.example.brushandcoloursBackend.activities.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ActivityResponse {

    private String id;

    private String title;

    private String description;

    private BigDecimal basePrice;

    private String category;

    private String city;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}
