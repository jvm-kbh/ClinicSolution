package me.kbh.clinicsolution.common.exception.code;

import org.springframework.http.HttpStatus;

public interface BaseError {

  HttpStatus getHttpStatus();

  String getMessage();
}
