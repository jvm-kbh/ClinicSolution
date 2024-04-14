package me.kbh.clinicsolution.common.code.system;

import me.kbh.clinicsolution.common.code.BaseEnum;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;

public interface SystemCodeBase extends BaseEnum {

  String getCode();

  SystemGroupCodeType getParent();
}
