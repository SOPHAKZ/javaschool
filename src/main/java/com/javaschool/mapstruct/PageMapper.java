package com.javaschool.mapstruct;


import com.javaschool.utils.PageDTO;
import com.javaschool.utils.PaginationDTO;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageMapper {

    PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

    @Mapping(target = "pagination",expression = "java(paginationDTO(page))")
    @Mapping(target = "content",expression = "java(page.getContent())")
    PageDTO pageDTO(Page<?> page);

    PaginationDTO paginationDTO(Page<?> page);
}
