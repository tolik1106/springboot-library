package com.zhitar.library.mapper;

public interface EntityMapper<DTO, Entity> {

    Entity toEntity(DTO dto);

    DTO toDto(Entity entity);
}
