package org.test;public class Test {
    private static int baz() {
        return 3 / 1;
    }

    private static int bar() {
        return baz();
    }

    private static int foo() {
        return bar();
    }

    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        int z = foo();
    }
}
