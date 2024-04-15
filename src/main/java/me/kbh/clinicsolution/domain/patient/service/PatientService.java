package me.kbh.clinicsolution.domain.patient.service;

import java.util.List;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;

public interface PatientService extends PatientDefaultExceptionSupplier {

  PatientResponse findById(Long patientId);

  List<PatientResponse> findAll();

  PatientResponse save(PatientSaveRequest patientSaveRequest);

  PatientResponse update(Long patientId, PatientUpdateRequest patientUpdateRequest);

  void delete(Long patientId);
}
