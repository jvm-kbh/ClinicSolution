package me.kbh.clinicsolution.domain.patient.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalResponse;
import me.kbh.clinicsolution.domain.patient.entity.Patient;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientResponse implements Serializable {

  Long patientId;
  String patientName;
  String patientRegistrationNumber;
  String genderCode;
  LocalDate birthDate;
  String phoneNumber;
  HospitalResponse hospital;

  @Builder
  protected PatientResponse(Patient mappingByEntity) {
    this.patientId = mappingByEntity.getPatientId();
    this.patientName = mappingByEntity.getPatientName();
    this.patientRegistrationNumber = mappingByEntity.getPatientRegistrationNumber();
    this.genderCode = mappingByEntity.getGenderCode();
    this.birthDate = mappingByEntity.getBirthDate();
    this.phoneNumber = mappingByEntity.getPhoneNumber();
    this.hospital = HospitalResponse.builder()
        .mappingByEntity(mappingByEntity.getHospital())
        .build();
  }
}
