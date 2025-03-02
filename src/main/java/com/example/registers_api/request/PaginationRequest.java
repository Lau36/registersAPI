package com.example.registers_api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaginationRequest {
    private int page;
    private int size;
    private String sort;
    private SortDirection sortDirection;
}
