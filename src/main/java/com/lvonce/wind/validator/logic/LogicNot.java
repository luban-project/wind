package com.lvonce.wind.validator.logic;

import com.lvonce.wind.validator.Validator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogicNot implements Validator {
    private Validator child;

    @Override
    public ValidateResult accept(Object val) {
        return child.accept(val);
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = this.child.validate();
        if (result.isSuccess()) {
            return ValidateResult.ofFailure("");
        } else {
            return ValidateResult.ofSuccess();
        }
    }
}
