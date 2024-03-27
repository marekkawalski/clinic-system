package com.marek_kawalski.clinic_system.examination;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {
    private final ExaminationRepository examinationRepository;

    @Override
    public Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams) {
        return examinationRepository.getPagedExaminations(examinationRequestParams);
    }
}
