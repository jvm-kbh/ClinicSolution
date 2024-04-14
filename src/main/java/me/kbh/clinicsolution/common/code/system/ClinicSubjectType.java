package me.kbh.clinicsolution.common.code.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;

@Getter
@AllArgsConstructor
public enum ClinicSubjectType implements SystemCodeBase {

  CLINIC_SUBJECT_ONE("01", "내과"),
  CLINIC_SUBJECT_TWO("02", "안과");

  private String code;
  private String name;

  @Override
  public SystemGroupCodeType getParent() {
    return SystemGroupCodeType.CLINIC_SUBJECT;
  }
}
