package me.kbh.clinicsolution.domain.patient.service;

import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.hospital.repository.HospitalRepository;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patient.repository.PatientRepository;
import me.kbh.clinicsolution.domain.patient.util.PatientUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientServiceImpl implements PatientService {

  PatientRepository patientRepository;
  HospitalRepository hospitalRepository;

  @Override
  public PatientResponse findById(Long hospitalId) {
    Patient patient =
        patientRepository.findById(hospitalId).orElseThrow(patientNotFoundException());

    return PatientResponse.builder().mappingByEntity(patient).build();
  }

  @Override
  public List<PatientResponse> findAll() {
    List<Patient> patientList = patientRepository.findAll();
    Function<Patient, PatientResponse> mappingByEntityFunction = patient -> PatientResponse.builder()
        .mappingByEntity(patient)
        .build();
    return patientList.stream()
        .map(mappingByEntityFunction)
        .toList();
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

    Patient patient =
        patientRepository.findById(patientId).orElseThrow(patientNotFoundException());

    patient.update(patientUpdateRequest, relateHospital);

    return PatientResponse.builder().mappingByEntity(patient).build();
  }

  @Override
  public void delete(Long patientId) {
    patientRepository.findById(patientId).orElseThrow(patientNotFoundException());
    patientRepository.deleteById(patientId);
  }
}
