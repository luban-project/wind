package com.lvonce.wind.processor;


import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.lvonce.wind.ConverterBuilder;
import com.lvonce.wind.ValidatorBuilder;
import com.lvonce.wind.converter.Converter;
import com.lvonce.wind.http.HttpRequestBody;
import com.lvonce.wind.sql.MybatisExecutor;
import com.lvonce.wind.validator.LengthValidator;
import com.lvonce.wind.validator.Validator;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;


public class AttrProcessor implements Processor {

    private final Converter converter;
    private ValidatorBuilder.ValidatorBuildArg validatorArg;
    private final Object attr;

    public AttrProcessor(Object attr, ConverterBuilder.TypeConverterBuildArg converterArg, ValidatorBuilder.ValidatorBuildArg validatorArg) {
        this.attr = attr;
        this.converter = ConverterBuilder.genTypeConverter(converterArg);
        this.validatorArg = validatorArg;
    }

    @Override
    public ProcessResult process() {
        Object converted = attr;
        if (converter != null) {
            converted = converter.converter(attr);
        }
        if (this.validatorArg != null) {
            Validator validator = ValidatorBuilder.genValidator(validatorArg);
            validator.accept(converted);
            Validator.ValidateResult result = Validator.ValidateResult.ofSuccess();
            if (validator != null) {
                result = validator.validate();
            }
            if (result.isSuccess()) {
                return ProcessResult.ofSuccess(converted);
            } else {
                return ProcessResult.ofFailure(result.getErrMsg());
            }
        } else {
            return ProcessResult.ofSuccess(converted);
        }

    }

}
