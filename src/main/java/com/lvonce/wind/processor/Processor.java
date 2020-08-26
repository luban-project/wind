package com.lvonce.wind.processor;

import com.lvonce.wind.validator.Validator;
import lombok.Getter;

public interface Processor {
    @Getter
    class ProcessResult {
        Object val;
        String errMsg;

        private ProcessResult(Object obj, String errMsg) {
            this.val = obj;
            this.errMsg = "Process Error: "+ errMsg;
        }
        public static ProcessResult ofSuccess(Object val) {
            return new ProcessResult(val, null);
        }
        public static ProcessResult ofFailure(String errMsg) {
            return new ProcessResult(null, errMsg);
        }

        public static ProcessResult ofResult(Object obj, String errMsg) {
            if (obj != null) {
                return ofSuccess(obj);
            } else {
                return ofFailure(errMsg);
            }
        }
        public boolean isSuccess() {
            return this.val != null;
        }
    }

    ProcessResult process();
}
