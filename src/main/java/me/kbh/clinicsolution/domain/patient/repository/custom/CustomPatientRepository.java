package me.kbh.clinicsolution.domain.patient.repository.custom;

import java.util.List;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.entity.Patient;

public interface CustomPatientRepository{
  List<Patient> findAllByCondition(PatientSearchCondition patientSearchCondition);
}
