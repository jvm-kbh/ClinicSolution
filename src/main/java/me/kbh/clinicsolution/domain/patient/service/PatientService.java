package me.kbh.clinicsolution.domain.patient.service;

import me.kbh.clinicsolution.common.dto.PageInfoWrapper;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.dto.PatientTotalInfoResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;
import org.springframework.data.domain.Pageable;

public interface PatientService extends PatientDefaultExceptionSupplier {

  PatientTotalInfoResponse findById(Long patientId);

  PageInfoWrapper<PatientResponse> findAllByCondition(PatientSearchCondition patientSearchCondition,
      Pageable pageable);

  PatientResponse save(PatientSaveRequest patientSaveRequest);

  PatientResponse update(Long patientId, PatientUpdateRequest patientUpdateRequest);

  void delete(Long patientId);
}
