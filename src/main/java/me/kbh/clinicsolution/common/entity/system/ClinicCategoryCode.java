package me.kbh.clinicsolution.common.entity.system;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.common.code.system.SystemCodeBase;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType.Group;
import me.kbh.clinicsolution.common.entity.system.group.SystemGroupCode;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = Group.CLINIC_CATEGORY)
public class ClinicCategoryCode extends SystemCode {

  @Builder
  protected ClinicCategoryCode(SystemGroupCode systemGroupCode, SystemCodeBase systemCode) {
    validSystemCodeGroupRule(systemGroupCode);
    validSystemCodeRule(systemCode);
    this.codeName = systemCode.getName();
    this.code = systemCode.getCode();
  }
}
