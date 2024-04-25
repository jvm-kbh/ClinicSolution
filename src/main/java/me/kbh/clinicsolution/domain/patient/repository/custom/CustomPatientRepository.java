package me.kbh.clinicsolution.domain.patient.repository.custom;

import com.querydsl.core.Tuple;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPatientRepository {

  Page<Tuple> findAllByCondition(PatientSearchCondition patientSearchCondition,
      Pageable pageable);
}
