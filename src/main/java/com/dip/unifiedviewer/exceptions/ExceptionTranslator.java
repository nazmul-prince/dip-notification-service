package com.dip.unifiedviewer.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dip.unifiedviewer.domain.model.ApiExposureError;
import com.dip.unifiedviewer.util.ResponseUtil;

@RestControllerAdvice
public class ExceptionTranslator {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiExposureError> handleValidationException(MethodArgumentNotValidException ex) {
    ApiExposureError error = new ApiExposureError(HttpStatus.BAD_REQUEST, getDefaultMessage(ex));
    return ResponseUtil.getResponseEntity(HttpStatus.BAD_REQUEST, new HttpHeaders(), error);
  }

  @ExceptionHandler(RequestForbiddenException.class)
  public ResponseEntity<ApiExposureError> handleRequestForbiddenException(RequestForbiddenException ex) {
    ApiExposureError error = new ApiExposureError(HttpStatus.FORBIDDEN, ex.getMessage());
    return ResponseUtil.getResponseEntity(HttpStatus.BAD_REQUEST, new HttpHeaders(), error);
  }

  @ExceptionHandler(RequestUnauthorizedException.class)
  public ResponseEntity<ApiExposureError> handleRequestUnauthorizedException(RequestUnauthorizedException ex) {
    ApiExposureError error = new ApiExposureError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return ResponseUtil.getResponseEntity(HttpStatus.BAD_REQUEST, new HttpHeaders(), error);
  }

  private String getDefaultMessage(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
  }
}
