package me.kbh.clinicsolution.domain.patientvisit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.code.system.ClinicCategoryType;
import me.kbh.clinicsolution.common.code.system.ClinicSubjectType;
import me.kbh.clinicsolution.common.code.system.VisitStatusType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientVisitSaveRequest {

  @NotNull(message = "방문 상태는 필수 값입니다.")
  VisitStatusType visitStatusType;

  @NotNull(message = "진료 과목은 필수 값입니다.")
  ClinicSubjectType clinicSubjectType;

  @NotNull(message = "진료 유형은 필수 값입니다.")
  ClinicCategoryType clinicCategoryType;

  @NotNull(message = "환자 ID는 필수 값입니다.")
  @Min(value = 0, message = "환자의 ID는 음수로 구성되어있지 않습니다.")
  Long patientId;

  @NotNull(message = "병원 ID는 필수 값입니다.")
  @Min(value = 0, message = "병원의 ID는 음수로 구성되어있지 않습니다.")
  Long hospitalId;

  @Builder
  protected PatientVisitSaveRequest(
      VisitStatusType visitStatusType,
      ClinicSubjectType clinicSubjectType,
      ClinicCategoryType clinicCategoryType,
      Long patientId,
      Long hospitalId
  ) {
    this.visitStatusType = visitStatusType;
    this.clinicSubjectType = clinicSubjectType;
    this.clinicCategoryType = clinicCategoryType;
    this.patientId = patientId;
    this.hospitalId = hospitalId;
  }
}
