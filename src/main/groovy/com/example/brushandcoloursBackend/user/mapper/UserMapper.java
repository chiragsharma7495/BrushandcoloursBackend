package com.example.brushandcoloursBackend.user.mapper;

import com.example.brushandcoloursBackend.user.AppUser;
import com.example.brushandcoloursBackend.user.dto.RegisterRequest;
import com.example.brushandcoloursBackend.user.dto.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AppUser toEntity(RegisterRequest request);

    RegisterResponse toResponse(AppUser user);
}
