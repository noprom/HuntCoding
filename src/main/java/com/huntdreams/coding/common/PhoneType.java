package com.huntdreams.coding.common;

/**
 * 手机类型
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
public class PhoneType {

    public static boolean isX86() {
        String arch = System.getProperty("os.arch").toLowerCase();
        return arch.equals("i686") || arch.equals("x86");
    }

}
