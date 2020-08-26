package com.lvonce.wind.validator;

import lombok.Getter;

public interface Validator {

    @Getter
    class ValidateResult {
        boolean success;
        String errMsg;
        private ValidateResult(boolean success, String errMsg) {
            this.success = success;
            this.errMsg = "Validate Error: "+ errMsg;
        }
        public static ValidateResult ofSuccess() {
            return new ValidateResult(true, null);
        }
        public static ValidateResult ofFailure(String errMsg) {
            return new ValidateResult(false, errMsg);
        }

        public static ValidateResult ofResult(boolean success, String errMsg) {
            if (success) {
                return ofSuccess();
            } else {
                return ofFailure(errMsg);
            }
        }
    }

    ValidateResult accept(Object val);

    ValidateResult validate();
}
