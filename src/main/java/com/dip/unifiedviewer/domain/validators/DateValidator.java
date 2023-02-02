package com.dip.unifiedviewer.domain.validators;

import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.dip.unifiedviewer.constansts.ApiExposureConstants;

public class DateValidator implements ConstraintValidator<ValidDate, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ApiExposureConstants.DATE_FORMAT);
        try{
            dateTimeFormatter.parse(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
