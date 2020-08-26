package com.lvonce.wind;

import com.lvonce.wind.validator.*;
import com.lvonce.wind.validator.logic.LogicAnd;
import com.lvonce.wind.validator.logic.LogicNot;
import com.lvonce.wind.validator.logic.LogicOr;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ValidatorBuilder {
    @Data
    public static class ValidatorBuildArg {
        private String validatorName;
        private Object[] args;
        public ValidatorBuildArg(String validatorName, Object... args) {
            this.validatorName = validatorName;
            this.args = args;
        }
    }

    static Integer parseToInt(Object val) {
        if (val instanceof String) {
            return Integer.parseInt((String) val);
        } else if (val instanceof Integer) {
            return (Integer) val;
        } else {
            return null;
        }
    }


    public static Validator genValidator(ValidatorBuildArg buildArg) {
        if (buildArg == null) {
            return null;
        }
        switch (buildArg.validatorName) {
            case "And":
                Validator[] andValidator = new Validator[buildArg.args.length];
                for (int i=0; i< buildArg.args.length; ++i) {
                    andValidator[i] = ValidatorBuilder.genValidator((ValidatorBuildArg) buildArg.args[i]);
                }
                return new LogicAnd(andValidator);
            case "Or":
                Validator[] orValidators = new Validator[buildArg.args.length];
                for (int i=0; i< buildArg.args.length; ++i) {
                    orValidators[i] = ValidatorBuilder.genValidator((ValidatorBuildArg) buildArg.args[i]);
                }
                return new LogicOr(orValidators);
            case "Not":
                Validator notValidators = ValidatorBuilder.genValidator((ValidatorBuildArg) buildArg.args[0]);
                return new LogicNot(notValidators);

            case "Email":
                return new EmailValidator();
            case "Number":
                return new NumberValidator();
            case "ChineseCellphone":
                return new ChineseCellphoneValidator();
            case "EnumString":
                return new EnumStrValidator((String[])buildArg.args);
            case "Length":
                if (buildArg.args.length == 1) {
                    int len = parseToInt(buildArg.args[0]);
                    return new LengthValidator(len, len);
                } else if (buildArg.args.length == 2) {
                    int min = parseToInt(buildArg.args[0]);
                    int max = parseToInt(buildArg.args[1]);
                    return new LengthValidator(min, max);
                } else {
                    return null;
                }
            case "Range":
                if (buildArg.args.length == 2) {
                    Comparable min = (Comparable) buildArg.args[0];
                    Comparable max = (Comparable) buildArg.args[1];
                    return new MinMaxRange(min, max);
                } else {
                    return null;
                }
            default:
                return null;
        }
    }
}
