package me.kbh.clinicsolution.domain.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.common.entity.BaseEntity;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.patient.dto.PatientSaveRequest;
import me.kbh.clinicsolution.domain.patient.dto.PatientUpdateRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "patient_registration_number_unique",
            columnNames = {"patient_registration_number", "hospital_id"})
    })
public class Patient extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long patientId;

  @Column(nullable = false, length = 45)
  String patientName;

  @Column(nullable = false, length = 13)
  String patientRegistrationNumber;

  @Column(nullable = false, length = 10)
  String genderCode;

  @Column(length = 10)
  LocalDate birthDate;

  @Column(length = 20)
  String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "hospital_id", nullable = false)
  Hospital hospital;

  @Builder(
      builderClassName = "saveBuilder",
      builderMethodName = "builderForSave",
      buildMethodName = "buildBySaveRequest")
  protected Patient(PatientSaveRequest patientSaveRequest, Hospital hospital,
      String patientRegistrationNumber) {
    this.patientName = patientSaveRequest.getPatientName();
    this.genderCode = patientSaveRequest.getGenderType().getName();
    this.birthDate = patientSaveRequest.getBirthDate();
    this.phoneNumber = patientSaveRequest.getPhoneNumber();
    this.hospital = hospital;
    this.patientRegistrationNumber = patientRegistrationNumber;
  }

  public void update(PatientUpdateRequest patientUpdateRequest,
      Hospital hospital) {
    this.patientName = patientUpdateRequest.getPatientName();
    this.genderCode = patientUpdateRequest.getGenderType().getName();
    this.birthDate = patientUpdateRequest.getBirthDate();
    this.phoneNumber = patientUpdateRequest.getPhoneNumber();
    this.hospital = hospital;
  }
}
