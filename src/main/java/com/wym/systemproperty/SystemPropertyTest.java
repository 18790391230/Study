package com.wym.systemproperty;

/**
 * -DconfigPath=abc.properties
 */
public class SystemPropertyTest {

    public static void main(String[] args) {
        System.out.println(System.getProperty("configPath"));
    }
}
