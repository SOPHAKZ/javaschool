package com.javaschool.utils;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    List<?> content;
    PaginationDTO pagination;
}
