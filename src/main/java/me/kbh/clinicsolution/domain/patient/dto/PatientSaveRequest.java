package me.kbh.clinicsolution.domain.patient.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.code.system.GenderType;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientSaveRequest {

  @NotNull(message = "환자 이름은 필수 값입니다.")
  String patientName;

  @NotNull(message = "성별 코드 값은 필수 값입니다.")
  GenderType genderType;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDate birthDate;

  @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "휴대폰 번호 양식에 맞지 않습니다.")
  String phoneNumber;

  @NotNull
  @Min(value = 0, message = "병원의 ID는 음수로 구성되어있지 않습니다.")
  Long hospitalId;

  @Builder
  protected PatientSaveRequest(
      String patientName,
      GenderType genderType,
      LocalDate birthDate,
      String phoneNumber,
      Long hospitalId
  ) {
    this.patientName = patientName;
    this.genderType = genderType;
    this.birthDate = birthDate;
    this.phoneNumber = phoneNumber;
    this.hospitalId = hospitalId;
  }
}
