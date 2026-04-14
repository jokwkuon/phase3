class RedefinitionTest {
    void m() {
        int x = 1;
        x = 5;
        int y = x + 2;
    }
}