package com.lvonce.wind.validator;


public class LengthValidator implements Validator {

    private final int minLength;
    private final boolean minExclusive;
    private final int maxLength;
    private final boolean maxExclusive;
    private String val;

    public LengthValidator(int minLength, int maxLength) {
        this.minLength = minLength;
        this.minExclusive = false;
        this.maxLength = maxLength;
        this.maxExclusive = false;
    }

    public LengthValidator(int minLength, boolean minExclusive, int maxLength, boolean maxExclusive) {
        this.minLength = minLength;
        this.minExclusive = minExclusive;
        this.maxLength = maxLength;
        this.maxExclusive = maxExclusive;
    }

    @Override
    public ValidateResult accept(Object val) {
        if (val instanceof String) {
            this.val = (String) val;
            return ValidateResult.ofSuccess();
        } else {
            return ValidateResult.ofFailure("need String type");
        }
    }

    @Override
    public ValidateResult validate() {
        if (val == null) {
            return ValidateResult.ofResult((minLength == 0) && (!minExclusive), "string should not be null");
        }
        int len = val.length();
        int maxDiff = len - maxLength;
        if (maxDiff > 0) {
            return ValidateResult.ofFailure("string length is greater than max value [" + maxLength + "]");
        }
        if (maxDiff == 0 && maxExclusive) {
            return ValidateResult.ofFailure("string length must lower than max value [" + maxLength + "]");
        }
        int minDiff = len - minLength;
        if (minDiff < 0) {
            return ValidateResult.ofFailure("string length is lower than min value [" + minLength + "]");
        }
        if (minDiff == 0 && minExclusive) {
            return ValidateResult.ofFailure("string length must greater than min value [" + minLength + "]");
        }
        return ValidateResult.ofSuccess();
    }
}
