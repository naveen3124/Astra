package com.astra.app.demoproc;

import com.astra.app.utils.JavaUtils;

public class DemoProc {
    static {
        JavaUtils.loadLibrary("libexample.so");
    }

    public static native void initialize();
    public static native String commit(String filepath);
}

