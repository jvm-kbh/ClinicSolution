package me.kbh.clinicsolution.domain.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalUpdateRequest {

  @NotNull(message = "병원 이름은 비어있을 수 없습니다.")
  @Size(max = 45,message = "병원 이름의 최대 길이는 45입니다.")
  String hospitalName;

  @NotNull(message = "기관 번호는 비어있을 수 없습니다.")
  @Size(max = 20,message = "기관 번호의 최대 길이는 20입니다.")
  String medicalInstitutionNumber;

  @NotNull(message = "병원장 이름은 비어있을 수 없습니다.")
  @Size(max = 10,message = "병원장 이름의 최대 길이는 10입니다.")
  String hospitalDirectorName;

}
