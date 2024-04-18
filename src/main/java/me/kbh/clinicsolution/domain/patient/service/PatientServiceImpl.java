package me.kbh.clinicsolution.domain.patient.service;

import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.dto.PageInfoWrapper;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.hospital.repository.HospitalRepository;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientSearchCondition;
import me.kbh.clinicsolution.domain.patient.dto.PatientTotalInfoResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patient.repository.PatientRepository;
import me.kbh.clinicsolution.domain.patient.util.PatientUtil;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;
import me.kbh.clinicsolution.domain.patientvisit.repository.PatientVisitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientServiceImpl implements PatientService {

  PatientRepository patientRepository;
  HospitalRepository hospitalRepository;
  PatientVisitRepository patientVisitRepository;

  @Override
  public PatientTotalInfoResponse findById(Long patientId) {
    Patient patient =
        patientRepository.findById(patientId).orElseThrow(patientNotFoundException());

    List<PatientVisit> patientVisitList = patientVisitRepository.findAllByPatientPatientId(
        patientId);

    return PatientTotalInfoResponse.builder()
        .mappingByEntity(patient)
        .patientVisitList(patientVisitList)
        .build();
  }

  @Override
  public PageInfoWrapper<PatientResponse> findAllByCondition(
      PatientSearchCondition patientSearchCondition,
      Pageable pageable
  ) {
    Page<Patient> patientPage = patientRepository.findAllByCondition(patientSearchCondition,
        pageable);
    Function<Patient, PatientResponse> mappingByEntityFunction = patient -> PatientResponse.builder()
        .mappingByEntity(patient)
        .build();

    List<PatientResponse> patientResponseList = patientPage.getContent().stream()
        .map(mappingByEntityFunction)
        .toList();

    return new PageInfoWrapper<>(patientResponseList, patientPage);
  }

  @Override
  public PatientResponse save(PatientSaveRequest patientSaveRequest) {

    Hospital relateHospital = hospitalRepository.findByHospitalIdWithPessimisticLock(
            patientSaveRequest.getHospitalId())
        .orElseThrow(hospitalNotException());

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    Patient patient =
        Patient.builderForSave()
            .patientSaveRequest(patientSaveRequest)
            .hospital(relateHospital)
            .patientRegistrationNumber(patientRegistrationNumber)
            .buildBySaveRequest();

    Patient savedPatient = patientRepository.save(patient);

    return PatientResponse.builder()
        .mappingByEntity(savedPatient)
        .build();
  }

  @Override
  public PatientResponse update(Long patientId, PatientUpdateRequest patientUpdateRequest) {

    Hospital relateHospital = hospitalRepository.findById(patientUpdateRequest.getHospitalId())
        .orElseThrow(hospitalNotException());

    relateHospital.increasePatientVisitCount();

    String patientRegistrationNumber = PatientUtil.generatePatientRegistrationNumber(
        relateHospital.getPatientVisitCount());

    Patient patient =
        patientRepository.findById(patientId).orElseThrow(patientNotFoundException());

    patient.update(patientUpdateRequest, relateHospital, patientRegistrationNumber);

    return PatientResponse.builder().mappingByEntity(patient).build();
  }

  @Override
  public void delete(Long patientId) {
    patientRepository.findById(patientId).orElseThrow(patientNotFoundException());
    patientRepository.deleteById(patientId);
  }
}
