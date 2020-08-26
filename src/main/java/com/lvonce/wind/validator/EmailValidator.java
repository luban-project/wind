package com.lvonce.wind.validator;

import java.util.regex.Pattern;

public class EmailValidator  implements Validator {
    static String emailPattern = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
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
        return ValidateResult.ofResult(pattern.get().matcher(val).matches(), val +" is not a valid email");
    }
}
