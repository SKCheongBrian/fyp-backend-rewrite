public class Test {
    private static int baz() {
        return 3 / 0;
    }

    private static int bar() {
        return baz();
    }

    private static int foo() {
        return bar();
    }

    public static void main(String[] args) {
        int x = foo();
    }
}