package com.lvonce.wind.validator;


import java.util.regex.Pattern;

public class ChineseCellphoneValidator implements Validator {
    static final String emailPattern = "^1\\d{10}$";

    ThreadLocal<Pattern> pattern = ThreadLocal.withInitial(()-> Pattern.compile(emailPattern));
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
        return ValidateResult.ofResult(pattern.get().matcher(val).matches(), val + " is not a valid Chinese cellphone number");
    }
}
