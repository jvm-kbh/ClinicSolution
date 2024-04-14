package me.kbh.clinicsolution.common.code.system.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kbh.clinicsolution.common.code.BaseEnum;

@Getter
@AllArgsConstructor
public enum SystemGroupCodeType implements BaseEnum {
  GENDER("성별코드"),
  VISIT_STATUS("방문상태코드"),
  CLINIC_SUBJECT("진료과목코드"),
  CLINIC_CATEGORY("진료유형코드");
  private String name;

  public static class Group {

    public static final String GENDER = "GENDER";
    public static final String VISIT_STATUS = "VISIT_STATUS";
    public static final String CLINIC_SUBJECT = "CLINIC_SUBJECT";
    public static final String CLINIC_CATEGORY = "CLINIC_CATEGORY";
  }

  public String getGroupType() {
    return switch (this) {
      case GENDER -> Group.GENDER;
      case VISIT_STATUS -> Group.VISIT_STATUS;
      case CLINIC_SUBJECT -> Group.CLINIC_SUBJECT;
      case CLINIC_CATEGORY -> Group.CLINIC_CATEGORY;
    };
  }
}
