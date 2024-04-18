package me.kbh.clinicsolution.domain.patient.dto;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientSearchCondition {

  String patientName;

  @Pattern(regexp = "^P\\d{12}$", message = "환자 등록번호 형식에 맞지 않습니다.")
  String patientRegistrationNumber;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDate birthDate;

  @Builder
  protected PatientSearchCondition(
      String patientName,
      String patientRegistrationNumber,
      LocalDate birthDate
  ) {
    this.patientName = patientName;
    this.patientRegistrationNumber = patientRegistrationNumber;
    this.birthDate = birthDate;
  }
}
