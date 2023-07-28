class ArrayMat {
    int[] array = new int[8];

    private void test() {
        int[] a = new int[8];
        for (int i = 0; i < 8; ++i) {
            a[i] = i;
        }
    }

    public static void main(String[] args)  {
        ArrayMat obj = new ArrayMat();
        long iterations = 0;
        while (iterations <= 20000) {
            obj.test();
            iterations++;
        }
    }
}
