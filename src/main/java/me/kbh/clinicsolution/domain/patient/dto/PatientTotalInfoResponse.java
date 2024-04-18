package me.kbh.clinicsolution.domain.patient.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitResponse;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientTotalInfoResponse implements Serializable {

  Long patientId;
  String patientName;
  String patientRegistrationNumber;
  String genderCode;
  LocalDate birthDate;
  String phoneNumber;
  HospitalResponse hospital;
  List<PatientVisitResponse> patientVisitResponseList;

  @Builder
  protected PatientTotalInfoResponse(
      Patient mappingByEntity,
      List<PatientVisit> patientVisitList
  ) {
    this.patientId = mappingByEntity.getPatientId();
    this.patientName = mappingByEntity.getPatientName();
    this.patientRegistrationNumber = mappingByEntity.getPatientRegistrationNumber();
    this.genderCode = mappingByEntity.getGenderCode();
    this.birthDate = mappingByEntity.getBirthDate();
    this.phoneNumber = mappingByEntity.getPhoneNumber();
    this.hospital = HospitalResponse.builder()
        .mappingByEntity(mappingByEntity.getHospital())
        .build();

    Function<PatientVisit, PatientVisitResponse> mappingByEntityFunction
        = patientVisit -> PatientVisitResponse.builder()
        .mappingByEntity(patientVisit)
        .build();

    this.patientVisitResponseList =
        patientVisitList.stream()
            .map(mappingByEntityFunction)
            .toList();
  }
}
