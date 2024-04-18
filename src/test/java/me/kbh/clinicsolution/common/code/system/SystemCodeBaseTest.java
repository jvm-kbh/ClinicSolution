package me.kbh.clinicsolution.common.code.system;

import static org.junit.jupiter.api.Assertions.assertThrows;

import me.kbh.clinicsolution.common.code.system.group.SystemGroupCodeType;
import me.kbh.clinicsolution.common.entity.system.GenderCode;
import me.kbh.clinicsolution.common.entity.system.VisitStatusCode;
import me.kbh.clinicsolution.common.entity.system.group.SystemGroupCode;
import me.kbh.clinicsolution.common.exception.response.system.SystemCodeBusinessException;
import me.kbh.clinicsolution.common.exception.response.system.SystemGroupCodeBusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@DisplayName("번외 - 시스템 코드 테스트")
class SystemCodeBaseTest {

  @Test
  @Order(1)
  @DisplayName("번외 - 시스템 코드 도메인 규칙, 시스템 코드 그룹에 해당하지 않는 시스템 코드는 등록 불가")
  void systemCodeDomainRule() {
    SystemGroupCode systemGroupCode = SystemGroupCode.builder()
        .systemGroupCodeType(SystemGroupCodeType.GENDER)
        .description("성별코드")
        .build();

    SystemCodeBase systemCodeBase = VisitStatusType.VISIT_STATUS_ONE;

    assertThrows(
        SystemCodeBusinessException.class, () -> GenderCode.builder()
            .systemGroupCode(systemGroupCode)
            .systemCode(systemCodeBase)
            .build()
    );
  }

  @Test
  @Order(2)
  @DisplayName("번외 - 시스템 그룹코드 도메인 규칙, 시스템 코드 그룹에 맞는 빌더 클래스로만 엔티티 생성 가능")
  void systemGroupCodeDomainRule() {
    SystemGroupCode systemGroupCode = SystemGroupCode.builder()
        .systemGroupCodeType(SystemGroupCodeType.GENDER)
        .description("성별코드")
        .build();

    SystemCodeBase systemCodeBase = GenderType.GENDER_FEMALE;

    assertThrows(
        SystemGroupCodeBusinessException.class, () -> VisitStatusCode.builder()
            .systemGroupCode(systemGroupCode)
            .systemCode(systemCodeBase)
            .build()
    );
  }
}