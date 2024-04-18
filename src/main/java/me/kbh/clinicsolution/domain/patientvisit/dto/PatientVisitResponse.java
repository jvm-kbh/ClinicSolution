package me.kbh.clinicsolution.domain.patientvisit.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.patient.dto.PatientResponse;
import me.kbh.clinicsolution.domain.patientvisit.entity.PatientVisit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientVisitResponse implements Serializable {

  Long patientVisitId;
  String visitStatusCode;
  String clinicSubjectCode;
  String clinicCategoryCode;
  PatientResponse patient;
  HospitalResponse hospital;
  LocalDate createAt;

  @Builder
  protected PatientVisitResponse(PatientVisit mappingByEntity) {

    this.patientVisitId = mappingByEntity.getPatientVisitId();
    this.visitStatusCode = mappingByEntity.getVisitStatusCode();
    this.clinicSubjectCode = mappingByEntity.getClinicSubjectCode();
    this.clinicCategoryCode = mappingByEntity.getClinicCategoryCode();

    this.patient = PatientResponse.builder()
        .mappingByEntity(mappingByEntity.getPatient())
        .build();
    this.hospital = HospitalResponse.builder()
        .mappingByEntity(mappingByEntity.getHospital())
        .build();
    Optional.ofNullable(mappingByEntity.getCreatedDate())
        .ifPresent(localDateTime -> this.createAt = localDateTime.toLocalDate());
  }
}
