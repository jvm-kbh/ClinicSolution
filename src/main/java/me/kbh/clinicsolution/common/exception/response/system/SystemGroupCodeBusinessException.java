package me.kbh.clinicsolution.common.exception.response.system;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.system.SystemGroupCodeError;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemGroupCodeBusinessException extends RuntimeException {

  SystemGroupCodeError systemGroupCodeError;
}
