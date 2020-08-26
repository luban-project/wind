package com.lvonce.wind.validator.logic;

import com.lvonce.wind.validator.Validator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogicAnd implements Validator {

    Validator[] children;

    @Override
    public ValidateResult accept(Object val) {
        for (Validator validator: children) {
            ValidateResult result = validator.accept(val);
            if (!result.isSuccess()) {
                return ValidateResult.ofFailure(result.getErrMsg());
            }
        }
        return ValidateResult.ofSuccess();
    }

    @Override
    public ValidateResult validate() {
        for (Validator node : children) {
            ValidateResult result = node.validate();
            if (!result.isSuccess()) {
                return ValidateResult.ofFailure(result.getErrMsg());
            }
        }
        return ValidateResult.ofSuccess();
    }
}
