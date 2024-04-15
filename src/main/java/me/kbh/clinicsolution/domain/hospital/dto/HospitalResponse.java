package me.kbh.clinicsolution.domain.hospital.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.domain.hospital.entity.Hospital;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalResponse implements Serializable {

  Long hospitalId;

  String hospitalName;

  String medicalInstitutionNumber;

  String hospitalDirectorName;

  @Builder
  protected HospitalResponse(Hospital mappingByEntity) {
    this.hospitalId = mappingByEntity.getHospitalId();
    this.hospitalName = mappingByEntity.getHospitalName();
    this.medicalInstitutionNumber = mappingByEntity.getMedicalInstitutionNumber();
    this.hospitalDirectorName = mappingByEntity.getHospitalDirectorName();
  }
}
