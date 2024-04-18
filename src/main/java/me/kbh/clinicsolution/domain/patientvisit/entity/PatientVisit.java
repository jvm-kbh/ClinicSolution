package me.kbh.clinicsolution.domain.patientvisit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.common.entity.BaseEntity;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;
import me.kbh.clinicsolution.domain.patient.entity.Patient;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitSaveRequest;
import me.kbh.clinicsolution.domain.patientvisit.dto.PatientVisitUpdateRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientVisit extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long patientVisitId;

  @Column(nullable = false)
  String visitStatusCode;

  @Column(nullable = false)
  String clinicSubjectCode;

  @Column(nullable = false)
  String clinicCategoryCode;

  @ManyToOne
  @JoinColumn(nullable = false, name = "patient_id")
  Patient patient;

  @ManyToOne
  @JoinColumn(nullable = false, name = "hospital_id")
  Hospital hospital;

  @Builder(
      builderClassName = "saveBuilder",
      builderMethodName = "builderForSave",
      buildMethodName = "buildBySaveRequest")
  protected PatientVisit(
      PatientVisitSaveRequest patientSaveRequest,
      Hospital hospital,
      Patient patient
  ) {
    this.visitStatusCode = patientSaveRequest.getVisitStatusType().getName();
    this.clinicSubjectCode = patientSaveRequest.getClinicSubjectType().getName();
    this.clinicCategoryCode = patientSaveRequest.getClinicCategoryType().getName();
    this.hospital = hospital;
    this.patient = patient;
  }

  public void update(
      PatientVisitUpdateRequest patientUpdateRequest,
      Hospital hospital,
      Patient patient) {
    this.visitStatusCode = patientUpdateRequest.getVisitStatusType().getName();
    this.clinicSubjectCode = patientUpdateRequest.getClinicSubjectType().getName();
    this.clinicCategoryCode = patientUpdateRequest.getClinicCategoryType().getName();
    this.hospital = hospital;
    this.patient = patient;
  }
}
