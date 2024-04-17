package me.kbh.clinicsolution.common.entity.system;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.common.code.system.SystemCodeBase;
import me.kbh.clinicsolution.common.entity.system.group.SystemGroupCode;
import me.kbh.clinicsolution.common.exception.code.system.SystemCodeError;
import me.kbh.clinicsolution.common.exception.code.system.SystemGroupCodeError;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeBusinessException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "system_code_group")
public abstract class SystemCode {

  @Id
  @Column(length = 20)
  String code;

  @Column(nullable = false)
  String codeName;

  @Transient
  protected void validSystemCodeGroupRule(SystemGroupCode toCheckSystemGroupCode) {
    DiscriminatorValue discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
    String groupName = discriminatorValue == null ? null : discriminatorValue.value();
    String checkGroupType = toCheckSystemGroupCode.getSystemGroupCodeType().getGroupType();

    if (!Objects.equals(groupName, checkGroupType)) {
      throw new SystemGroupCodeBusinessException(SystemGroupCodeError.VALID_SYSTEM_CODE_GROUP);
    }
  }

  @Transient
  protected void validSystemCodeRule(SystemCodeBase systemCode) {
    DiscriminatorValue discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
    String groupName = discriminatorValue == null ? null : discriminatorValue.value();
    String checkGroupType = systemCode.getParent().getGroupType();

    if (!Objects.equals(groupName, checkGroupType)) {
      throw new SystemCodeBusinessException(SystemCodeError.VALID_SYSTEM_CODE);
    }
  }
}
