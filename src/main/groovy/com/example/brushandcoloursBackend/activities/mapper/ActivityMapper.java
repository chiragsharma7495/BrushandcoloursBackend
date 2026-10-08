package com.example.brushandcoloursBackend.activities.mapper;

import com.example.brushandcoloursBackend.activities.Activity;
import com.example.brushandcoloursBackend.activities.dto.ActivityCreateRequest;
import com.example.brushandcoloursBackend.activities.dto.ActivityResponse;
import com.example.brushandcoloursBackend.activities.dto.ActivityUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    @Mapping(target = "id",ignore = true )
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Activity toEntity(ActivityCreateRequest request);

    ActivityResponse toResponse(Activity activity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(                                    // understand again why did we use this method here
            ActivityUpdateRequest request,
            @MappingTarget Activity activity
    );
}
