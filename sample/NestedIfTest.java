class NestedIfTest {
    void m() {
        int x = 0;
        int y = 0;
        if (x == 0) {
            if (y == 0) {
                x = 1;
            }
        }
    }
}