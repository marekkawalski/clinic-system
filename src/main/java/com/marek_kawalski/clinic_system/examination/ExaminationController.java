package com.marek_kawalski.clinic_system.examination;

import com.marek_kawalski.clinic_system.examination.dto.ExaminationDTO;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/examinations")
@AllArgsConstructor
public class ExaminationController {
    private final ExaminationService examinationService;
    private final ExaminationMapper examinationMapper;

    @GetMapping("paged")
    public ResponseEntity<Page<ExaminationDTO>> getPagedExaminations(
            @RequestParam(value = "sort", defaultValue = "name") final String sortField,
            @RequestParam(value = "sort-dir", defaultValue = "ASC") final Sort.Direction sortDirection,
            @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
            @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "doctor-ids", required = false) final List<ObjectId> doctorIds
    ) {
        final Page<Examination> pagedExaminations = examinationService.getPagedExaminations(ExaminationRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .search(search)
                .doctorIds(doctorIds)
                .build());

        return pagedExaminations.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(pagedExaminations.map(examinationMapper::mapExaminationToExaminationDTO));

    }

}
