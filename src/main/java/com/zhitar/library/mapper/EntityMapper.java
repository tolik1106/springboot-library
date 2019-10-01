package com.zhitar.library.mapper;

import java.util.List;

public interface EntityMapper<DTO, Entity> {

    Entity toEntity(DTO dto);

    DTO toDto(Entity entity);

    List<Entity> toEntity(List<DTO> dtos);

    List<DTO> toDto(List<Entity> entities);
}
