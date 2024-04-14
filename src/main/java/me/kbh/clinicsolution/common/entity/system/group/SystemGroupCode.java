package me.kbh.clinicsolution.common.entity.system.group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SystemGroupCode {

  @Id
  @Enumerated(value = EnumType.STRING)
  @Column(length = 20)
  private SystemGroupCodeType systemGroupCodeType;
  private String codeGroupName;
  private String description;

  @Builder
  protected SystemGroupCode(SystemGroupCodeType systemGroupCodeType, String description) {
    this.systemGroupCodeType = systemGroupCodeType;
    this.codeGroupName = systemGroupCodeType.getName();
    this.description = description;
  }
}
