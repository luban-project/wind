import com.lvonce.wind.ConverterBuilder;
import com.lvonce.wind.ValidatorBuilder;
import com.lvonce.wind.processor.AttrMapProcessor;
import com.lvonce.wind.processor.Processor;
import com.lvonce.wind.validator.logic.LogicAnd;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessorTest {

    @Test
    public void test() {
        Map<String, Object> attrMap = new LinkedHashMap<>();
        attrMap.put("birthday", "2020-12-23 00:00:00");
        attrMap.put("email", "sdfsd@163.com");
        attrMap.put("cellphone", "12345678901");

        List<AttrMapProcessor.AttrProcessorArg> processorArgs = new ArrayList<>();
        processorArgs.add(new AttrMapProcessor.AttrProcessorArg(
                "birthday", "Birthday",
                new ConverterBuilder.TypeConverterBuildArg("String", "LocalDateTime", "yyyy-MM-dd HH:mm:ss"),

                new ValidatorBuilder.ValidatorBuildArg("Range",
                        LocalDateTime.parse("1999-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.parse("2050-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
        );
        processorArgs.add(
                new AttrMapProcessor.AttrProcessorArg(
                "email", "Email",
                null,
                new ValidatorBuilder.ValidatorBuildArg("And",
                        new ValidatorBuilder.ValidatorBuildArg("Email"),
                        new ValidatorBuilder.ValidatorBuildArg("Length", 10, 20))
        ));
        processorArgs.add(new AttrMapProcessor.AttrProcessorArg(
                "cellphone", "Cellphone",
                null,
                new ValidatorBuilder.ValidatorBuildArg("ChineseCellphone")
        ));
        Processor processor = new AttrMapProcessor(attrMap, processorArgs);
        Processor.ProcessResult result = processor.process();
        Assert.assertNotNull(result.getVal());
    }

}
