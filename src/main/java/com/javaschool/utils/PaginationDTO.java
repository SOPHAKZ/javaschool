package com.javaschool.utils;

import lombok.Data;

@Data
public class PaginationDTO {
    private int numberOfElements;
    private int size;
    private long totalElements;
    private int totalPage;
    private boolean empty;
    private boolean first;
    private boolean last;

}