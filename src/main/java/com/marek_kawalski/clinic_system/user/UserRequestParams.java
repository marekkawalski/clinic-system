package com.marek_kawalski.clinic_system.user;

import lombok.Builder;
import org.springframework.data.domain.Sort;

@Builder
public record UserRequestParams(
        String sortField,
        Sort.Direction sortDirection,
        Integer pageSize,
        Integer pageNumber,
        String search,
        UserRole role
) {
}
