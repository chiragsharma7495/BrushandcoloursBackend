package com.example.brushandcoloursBackend.activities.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivityCreateRequest {

    @NotBlank(message = "Title is Required")
    private String title;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotNull(message = "Base Price is Required")

    private BigDecimal basePrice;

    private String category;

    private boolean active;

    private String city;
}
