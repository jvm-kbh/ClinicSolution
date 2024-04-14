package me.kbh.clinicsolution.common.code.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;

@Getter
@AllArgsConstructor
public enum GenderType implements SystemCodeBase {

  GENDER_MAN("M", "남"),
  GENDER_FEMALE("F", "여"),
  GENDER_UNCLASSIFIED("U", "모름");

  private String code;
  private String name;

  @Override
  public SystemGroupCodeType getParent() {
    return SystemGroupCodeType.GENDER;
  }
}
