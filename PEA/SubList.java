import java.util.ArrayList;
import java.util.List;

// Based on:
// https://github.com/openjdk/jdk/blob/864b39a89398731bfde9af10c3d7797ff5d05760/test/jdk/java/lang/invoke/MethodHandlesSpreadArgumentsTest.java#L136-L142

class SubList {
    final static int NUM_ELEM = 25;
    public void test(ArrayList<Integer> A) {
        ArrayList<Integer> newA = new ArrayList<>(A);
        List<Integer> subList = newA.subList(3, 5);
        subList.clear();
        subList.add(2);
        assert newA.size() == 24 : "subList error, expected size 24, but got " + newA.size();
    }
    public static void main(String[] args) {
        SubList sl = new SubList();
        ArrayList<Integer> A = new ArrayList<>();
        for (int i = 0; i < NUM_ELEM; ++i) {
            A.add(1);
        }
        for (int i = 0; i < 20000; ++i) {
            sl.test(A);
        }
    }
}
