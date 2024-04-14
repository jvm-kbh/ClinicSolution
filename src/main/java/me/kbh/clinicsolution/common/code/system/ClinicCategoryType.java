package me.kbh.clinicsolution.common.code.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;

@Getter
@AllArgsConstructor
public enum ClinicCategoryType implements SystemCodeBase {

  CLINIC_CATEGORY_D("D", "약처방"),
  CLINIC_CATEGORY_T("T", "검사");

  private String code;
  private String name;

  @Override
  public SystemGroupCodeType getParent() {
    return SystemGroupCodeType.CLINIC_CATEGORY;
  }
}
