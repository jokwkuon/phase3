class TryCatchTest {
    void m() {
        int x = 1;
        try {
            x = x + 1;
        } catch (Exception e) {
            x = 0;
        }
    }
}