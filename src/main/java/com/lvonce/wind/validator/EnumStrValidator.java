package com.lvonce.wind.validator;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class EnumStrValidator implements Validator {
    private final Set<String> enumStrSet;
    private final boolean ignoreCase;
    private final String nullAsStr;
    private String val;

    public EnumStrValidator(String... stringList) {
        this.enumStrSet = new LinkedHashSet<>();
        this.enumStrSet.addAll(Arrays.asList(stringList));
        this.ignoreCase = false;
        this.nullAsStr = "";
    }

    public EnumStrValidator(String[] stringList, boolean ignoreCase, String nullAsStr) {
        this.enumStrSet = new LinkedHashSet<>();
        this.enumStrSet.addAll(Arrays.asList(stringList));
        this.ignoreCase = ignoreCase;
        this.nullAsStr = nullAsStr;
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
        String val = this.val == null? nullAsStr: this.val;
        if (ignoreCase) {
            for (String str : enumStrSet) {
                if (str.equalsIgnoreCase(val)) {
                    return ValidateResult.ofSuccess();
                }
            }
        } else {
            for (String str : enumStrSet) {
                if (str.equals(val)) {
                    return ValidateResult.ofSuccess();
                }
            }
        }
        return ValidateResult.ofFailure(val + "is not any of the enum string");
    }
}
