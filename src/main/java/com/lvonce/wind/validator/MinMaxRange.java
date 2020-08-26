package com.lvonce.wind.validator;

public class MinMaxRange<T extends Comparable<T>> implements Validator {

    private final T max;
    private final boolean maxExclusive;
    private final T min;
    private final boolean minExclusive;
    private T val;

    public MinMaxRange(T min, T max) {
        this.min = min;
        this.max = max;
        this.minExclusive = false;
        this.maxExclusive = false;
    }

    public MinMaxRange(T min, boolean minExclusive, T max, boolean maxExclusive) {
        this.min = min;
        this.minExclusive = minExclusive;
        this.max = max;
        this.maxExclusive = maxExclusive;
    }

    @Override
    public ValidateResult accept(Object val) {
        if (val instanceof Comparable) {
            this.val = (T) val;
            return ValidateResult.ofSuccess();
        } else {
            return ValidateResult.ofFailure("need comparable type");
        }
    }

    @Override
    public ValidateResult validate() {
        if (val == null) {
            return ValidateResult.ofFailure("null value");
        }
        int maxDiff = val.compareTo(max);
        if (maxDiff > 0) {
            return ValidateResult.ofFailure("value is greater than max value [" + val.toString() + "]");
        }
        if (maxDiff == 0 && maxExclusive) {
            return ValidateResult.ofFailure("value must lower than max value [" + val.toString() + "]");
        }
        int minDiff = val.compareTo(min);
        if (minDiff < 0) {
            return ValidateResult.ofFailure("value is lower than min value [" + val.toString() + "]");
        }
        if (minDiff == 0 && minExclusive) {
            return ValidateResult.ofFailure("value must greater than max value [" + val.toString() + "]");
        }
        return ValidateResult.ofSuccess();
    }
}
