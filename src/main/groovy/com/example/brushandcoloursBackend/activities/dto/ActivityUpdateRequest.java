package com.example.brushandcoloursBackend.activities.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivityUpdateRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Size(max = 200, message = "Description length cannot exceed 200")
    private String description;

    @NotNull(message = "Base Price is Required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base Price cannot be Negative")
    private BigDecimal basePrice;

    private String category;

    private String city;

    private boolean active;
}
