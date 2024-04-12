package me.kbh.clinicsolution.common.configuration;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class AppControllerAdvice {

  @InitBinder
  public void requestDirectFieldAccessBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }
}
