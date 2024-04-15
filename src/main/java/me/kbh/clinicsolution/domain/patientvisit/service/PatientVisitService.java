package me.kbh.clinicsolution.domain.patientvisit.service;

import java.util.List;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitResponse;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitSaveRequest;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitUpdateRequest;

public interface PatientVisitService extends PatientVisitDefaultExceptionSupplier {

  PatientVisitResponse findById(Long patientVisitId);

  List<PatientVisitResponse> findAll();

  PatientVisitResponse save(PatientVisitSaveRequest patientVisitSaveRequest);

  PatientVisitResponse update(Long patientVisitId,
      PatientVisitUpdateRequest patientVisitUpdateRequest);

  void delete(Long patientVisitId);
}
