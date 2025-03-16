package com.example.registers_api.response;

import com.example.registers_api.models.RegisterCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaginatedResponse {
    private List<RegistersResponse> registers;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
