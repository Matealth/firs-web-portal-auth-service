package com.firs.risk.mgt.auth.svc.validator;

import com.firs.risk.mgt.auth.svc.annotation.Phone;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone paramA) {
    }

    @Override
    public boolean isValid(String number, ConstraintValidatorContext ctx) {
        try{
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumber(phoneNumber); /*&& (phoneNumberUtil.getNumberType(phoneNumber) == PhoneNumberUtil.PhoneNumberType.MOBILE);*/
        }
        catch (NumberParseException e){
            return false;
        }
    }
}