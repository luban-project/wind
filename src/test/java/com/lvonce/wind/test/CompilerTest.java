package com.lvonce.wind.test;
import com.lvonce.wind.compiler.CompilerOfJava;
import com.lvonce.wind.compiler.Compiler;
import com.lvonce.wind.compiler.CompilerOfJavaString;
import com.lvonce.wind.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class CompilerTest  {

    public interface TestInterface {
        int add(int a, int b);
    }

    @Test
    public void testCompileJava() throws Exception {
        CompilerOfJavaString compiler = new CompilerOfJavaString();
        String source = "package xxx.example;" +
                "import com.lvonce.wind.test.CompilerTest.TestInterface;" +
                " public class Hello implements TestInterface {\n" +
                "    public int add(int a, int b) {\n" +
                "        return a + b;\n" +
                "    }\n" +
                "}\n";


        Map<String, byte[]> classMap = compiler.compile("Hello.java", source);
        Class<?> clazz = compiler.loadClass("xxx.example.Hello", classMap);
        Object obj = ReflectUtil.createInstance(clazz);
        Assert.assertNotNull(obj);
        TestInterface test = (TestInterface) obj;
        int result = test.add(3, 4);
        Assert.assertEquals(7, result);
    }
}
