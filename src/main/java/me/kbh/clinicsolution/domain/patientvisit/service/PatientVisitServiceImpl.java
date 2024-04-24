package me.kbh.clinicsolution.domain.patientvisit.service;

import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.hospital.repository.HospitalRepository;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patient.repository.PatientRepository;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitResponse;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitSaveRequest;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitUpdateRequest;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;
import me.kbh.clinicsolution.domain.patientvisit.repository.PatientVisitRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientVisitServiceImpl implements PatientVisitService {

  PatientVisitRepository patientVisitRepository;
  PatientRepository patientRepository;
  HospitalRepository hospitalRepository;

  @Override
  public PatientVisitResponse findById(Long patientVisitId) {
    PatientVisit patientVisit =
        patientVisitRepository.findById(patientVisitId).orElseThrow(patientNotFoundException());

    return PatientVisitResponse.builder().mappingByEntity(patientVisit).build();
  }

  @Override
  public List<PatientVisitResponse> findAll() {
    List<PatientVisit> patientVisitList = patientVisitRepository.findAll();

    Function<PatientVisit, PatientVisitResponse> mappingByEntityFunction =
        patientVisit -> PatientVisitResponse.builder()
            .mappingByEntity(patientVisit)
            .build();

    return patientVisitList.stream()
        .map(mappingByEntityFunction)
        .toList();
  }

  @Override
  public PatientVisitResponse save(PatientVisitSaveRequest patientVisitSaveRequest) {

    Patient relatePatient = patientRepository.findById(patientVisitSaveRequest.getPatientId())
        .orElseThrow(patientNotFoundException());

    Hospital relateHospital = hospitalRepository.findById(
            patientVisitSaveRequest.getHospitalId())
        .orElseThrow(hospitalNotException());

    PatientVisit patientVisit =
        PatientVisit.builderForSave()
            .patientSaveRequest(patientVisitSaveRequest)
            .hospital(relateHospital)
            .patient(relatePatient)
            .buildBySaveRequest();

    PatientVisit savedPatientVisit = patientVisitRepository.save(patientVisit);

    return PatientVisitResponse.builder()
        .mappingByEntity(savedPatientVisit)
        .build();
  }

  @Override
  public PatientVisitResponse update(Long patientId,
      PatientVisitUpdateRequest patientVisitUpdateRequest) {

    Patient relatePatient = patientRepository.findById(patientVisitUpdateRequest.getPatientId())
        .orElseThrow(patientNotFoundException());

    Hospital relateHospital = hospitalRepository.findById(patientVisitUpdateRequest.getHospitalId())
        .orElseThrow(hospitalNotException());

    PatientVisit patientVisit = patientVisitRepository.findById(patientId)
        .orElseThrow(patientVisitNotFoundException());

    patientVisit.update(patientVisitUpdateRequest, relateHospital, relatePatient);

    return PatientVisitResponse.builder().mappingByEntity(patientVisit).build();
  }

  @Override
  public void delete(Long patientVisitId) {
    patientVisitRepository.findById(patientVisitId).orElseThrow(patientVisitNotFoundException());
    patientVisitRepository.deleteById(patientVisitId);
  }
}
