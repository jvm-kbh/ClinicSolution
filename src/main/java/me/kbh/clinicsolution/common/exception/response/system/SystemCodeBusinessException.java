package me.kbh.clinicsolution.common.exception.response.system;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.kbh.clinicsolution.common.exception.code.system.SystemCodeError;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemCodeBusinessException extends RuntimeException{
  SystemCodeError systemCodeError;
}
