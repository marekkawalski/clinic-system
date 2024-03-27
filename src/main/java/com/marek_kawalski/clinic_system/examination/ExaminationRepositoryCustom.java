package com.marek_kawalski.clinic_system.examination;

import org.springframework.data.domain.Page;

public interface ExaminationRepositoryCustom {
    Page<Examination> getPagedExaminations(final ExaminationRequestParams examinationRequestParams);
}
