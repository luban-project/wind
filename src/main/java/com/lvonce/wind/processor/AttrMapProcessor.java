package com.lvonce.wind.processor;

import com.lvonce.wind.ConverterBuilder;
import com.lvonce.wind.ValidatorBuilder;
import com.lvonce.wind.converter.Converter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttrMapProcessor implements Processor {

    @Data
    public static class AttrProcessorArg {
        String oldName;
        String newName;
        ConverterBuilder.TypeConverterBuildArg converterArg;
        ValidatorBuilder.ValidatorBuildArg validatorArg;

        public AttrProcessorArg(String oldName,
                                String newName,
                                ConverterBuilder.TypeConverterBuildArg converterArg,
                                ValidatorBuilder.ValidatorBuildArg validatorArg) {
            this.oldName = oldName;
            this.newName = newName;
            this.converterArg = converterArg;
            this.validatorArg = validatorArg;
        }
        public AttrProcessorArg(String oldName,
                                ConverterBuilder.TypeConverterBuildArg converterArg,
                                ValidatorBuilder.ValidatorBuildArg validatorArg) {
            this.oldName = oldName;
            this.newName = oldName;
            this.converterArg = converterArg;
            this.validatorArg = validatorArg;
        }
    }

    Map<String, Object> attrMap;
    Map<String, AttrProcessorArg> processorArgMap = new LinkedHashMap<>();

    public AttrMapProcessor(Map<String, Object> attrMap, List<AttrProcessorArg> argList) {
        this.attrMap = attrMap;
        for (AttrProcessorArg arg : argList) {
            processorArgMap.put(arg.oldName, arg);
        }
    }

    @Override
    public ProcessResult process() {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry: attrMap.entrySet()) {
            String attrName = entry.getKey();
            Object val = entry.getValue();
            AttrProcessorArg processorArg = processorArgMap.get(attrName);
            AttrProcessor processor = new AttrProcessor(val, processorArg.converterArg, processorArg.validatorArg);
            ProcessResult processResult = processor.process();
            if (!processResult.isSuccess()) {
                return ProcessResult.ofFailure(processResult.errMsg);
            } else {
                resultMap.put(processorArg.newName, processResult.val);
            }
        }
        return ProcessResult.ofSuccess(resultMap);
    }
}
