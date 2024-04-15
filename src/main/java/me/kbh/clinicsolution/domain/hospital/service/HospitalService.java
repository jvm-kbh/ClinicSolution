package me.kbh.clinicsolution.domain.hospital.service;

import java.util.List;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalUpdateRequest;

public interface HospitalService extends HospitalDefaultExceptionSupplier {

  HospitalResponse findById(Long hospitalId);

  List<HospitalResponse> findAll();

  HospitalResponse save(HospitalSaveRequest hospitalSaveRequest);

  HospitalResponse update(Long hospitalId, HospitalUpdateRequest hospitalUpdateRequest);

  void delete(Long hospitalId);

  void increaseHospitalPatientVisit(Long hospitalId);
}
