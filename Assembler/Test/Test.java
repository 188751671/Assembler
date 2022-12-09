class Test {
    CodeTranslationUtils ct = new CodeTranslationUtils();

    @org.junit.jupiter.api.Test
    void CodeTranslationUtilsTest() {
        assert(ct.isRegister("R1"));

        assert (ct.isConstant("#123aaa123"));
    }
}