package com.javaschool.utils;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.Map;

public interface PageUtil {
    int PAGE_SIZE_DEFAULT = 10;
    int PAGE_NUMBER_DEFAULT = 1;
    String SORT_FIELD_DEFAULT = "id";
    String SORT_DIRECT_DEFAULT = "DESC";
    String PAGE_SIZE = "size";
    String PAGE_NUMBER = "page";
    String SORT_DIRECT = "sortDir";
    String SORT_BY = "sortBy";

    static Pageable getPageable(Map<String, String> params) {
        int pageNumber = Integer.parseInt(params.getOrDefault(PAGE_NUMBER, String.valueOf(PAGE_NUMBER_DEFAULT)));
        int pageSize = Integer.parseInt(params.getOrDefault(PAGE_SIZE, String.valueOf(PAGE_SIZE_DEFAULT)));

        String sortBy = StringUtils.hasText(params.get(SORT_BY)) ? params.get(SORT_BY) : SORT_FIELD_DEFAULT;
        String sortDirect = StringUtils.hasText(params.get(SORT_DIRECT)) ? params.get(SORT_DIRECT) : SORT_DIRECT_DEFAULT;

        pageNumber = Math.max(pageNumber, PAGE_NUMBER_DEFAULT);
        pageSize = Math.max(pageSize, PAGE_SIZE_DEFAULT);

        Sort.Direction sortDirection = getSortDirection(sortDirect);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortDirection, sortBy);
        return pageable;
    }

    static Sort.Direction getSortDirection(String direction) {
        return "DESC".equals(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }


}
