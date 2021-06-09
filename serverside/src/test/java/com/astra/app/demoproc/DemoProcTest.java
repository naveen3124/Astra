package com.astra.app.demoproc;

import org.junit.Test;
import com.astra.app.demoproc.DemoProc;

public class DemoProcTest {
    @Test
    public void testDemoProc() {
	        System.out.println("class path is " + System.getProperty("java.class.path"));

        DemoProc.initialize();
        String res = DemoProc.commit("Hello");
        System.out.println(res + '\n');
    }
}
