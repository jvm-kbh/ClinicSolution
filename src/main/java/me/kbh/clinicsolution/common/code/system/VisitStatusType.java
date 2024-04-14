package me.kbh.clinicsolution.common.code.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;

@Getter
@AllArgsConstructor
public enum VisitStatusType implements SystemCodeBase {

  VISIT_STATUS_ONE("1", "방문중"),
  VISIT_STATUS_TWO("2", "종료"),
  VISIT_STATUS_THREE("3", "취소");

  private String code;
  private String name;

  @Override
  public SystemGroupCodeType getParent() {
    return SystemGroupCodeType.VISIT_STATUS;
  }
}
