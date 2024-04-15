package me.kbh.clinicsolution.domain.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalSaveRequest;
import me.kbh.clinicsolution.domain.hospital.dto.HospitalUpdateRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long hospitalId;

  @Column(nullable = false, length = 45)
  String hospitalName;

  @Column(nullable = false, length = 20)
  String medicalInstitutionNumber;

  @Column(nullable = false, length = 10)
  String hospitalDirectorName;

  @Column(nullable = false, length = 12)
  Long patientVisitCount = 0L;

  @Builder(
      builderClassName = "saveBuilder",
      builderMethodName = "builderForSave",
      buildMethodName = "buildBySaveRequest")
  protected Hospital(HospitalSaveRequest hospitalSaveRequest) {
    this.hospitalName = hospitalSaveRequest.getHospitalName();
    this.medicalInstitutionNumber = hospitalSaveRequest.getMedicalInstitutionNumber();
    this.hospitalDirectorName = hospitalSaveRequest.getHospitalDirectorName();
  }

  public void update(HospitalUpdateRequest hospitalUpdateRequest) {
    this.hospitalName = hospitalUpdateRequest.getHospitalName();
    this.medicalInstitutionNumber = hospitalUpdateRequest.getMedicalInstitutionNumber();
    this.hospitalDirectorName = hospitalUpdateRequest.getHospitalDirectorName();
  }

  public void increasePatientVisitCount() {
    this.patientVisitCount++;
  }
}
