package com.lvonce.wind;

import com.lvonce.wind.converter.Converter;
import com.lvonce.wind.converter.LocalDateTimeConverterMap;
import com.lvonce.wind.converter.StringConverterMap;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class ConverterBuilder {
    @Data
    public static class TypeConverterBuildArg {
        private String sourceType;
        private String targetType;
        private String[] args;
        public TypeConverterBuildArg(String sourceType, String targetType, String ...args) {
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.args = args;
        }
    }

    /**
     * url: www.example.com/api/Hello
     * method: POST
     * name: {birth_day -> birthday}/{old => new}
     * type: [String -> LocalDateTime]{"yyyy-MM-dd"}
     * check: (<ChineseCellPhone> or <Email>) and <Range>{1900, 2035}
     * template(mybatis/handlerbars): select * from person where birthday = ${birthday}
     * result: {age->Age, name->Name, gender=>Sex}
     * @param buildArg
     * @return
     */
    public static Converter genTypeConverter(TypeConverterBuildArg buildArg) {
        if (buildArg == null) {
            return null;
        }
        String converterName = buildArg.sourceType + "To" + buildArg.targetType;
        switch (converterName) {
            case "StringToByte": return StringConverterMap.getFromStrConverter("String", "Byte");
            case "StringToShort": return StringConverterMap.getFromStrConverter("String", "Short");
            case "StringToInt": return StringConverterMap.getFromStrConverter("String", "Int");
            case "StringToLong": return StringConverterMap.getFromStrConverter("String", "Long");
            case "StringToFloat": return StringConverterMap.getFromStrConverter("String", "Float");
            case "StringToDouble": return StringConverterMap.getFromStrConverter("String", "Double");
            case "StringToBigDecimal": return StringConverterMap.getFromStrConverter("String", "BigDecimal");
            case "StringToBoolean": return StringConverterMap.getFromStrConverter("String", "Boolean");
            case "ToString": return StringConverterMap.getToStrConverter();
            case "LocalDateTimeToString": return LocalDateTimeConverterMap.getToStrConverter(buildArg.args[0]);
            case "StringToLocalDateTime": return LocalDateTimeConverterMap.getFromStrConverter(buildArg.args[0]);
            default: throw new RuntimeException("");
        }
    }



}
