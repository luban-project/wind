package com.lvonce.wind.validator;

import org.apache.commons.lang3.math.NumberUtils;

public class NumberValidator implements Validator {
    private String val;

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
            return ValidateResult.ofFailure("null value");
        }
        boolean success = NumberUtils.isCreatable(val);
        return ValidateResult.ofResult(success, val + " can not parsed to a number");
    }
}
