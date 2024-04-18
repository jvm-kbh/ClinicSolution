package me.kbh.clinicsolution.domain.patient.repository.custom;

import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPatientRepository {

  Page<Patient> findAllByCondition(PatientSearchCondition patientSearchCondition,
      Pageable pageable);
}
