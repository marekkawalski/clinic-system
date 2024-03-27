package com.marek_kawalski.clinic_system.examination;

import org.springframework.data.domain.Page;

public interface ExaminationService {
    Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams);
}
