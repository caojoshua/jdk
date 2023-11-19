import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

class SpliteratorMat {

    Stream<Path> getStream() {
        ArrayList<Path> arr = new ArrayList<>();
        return arr.stream();
    }

    void blackhole(byte[] data) {}

    public void test() throws IOException {
        // HashMap<String, ClassInfo> deps = new HashMap<>();

        // FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        FileSystem fs = FileSystems.getDefault();
        try (Stream<Path> s = Files.walk(fs.getPath("."))) {
            for (Path p : (Iterable<Path>)s::iterator) {
                if (p.toString().endsWith(".class") &&
                    !p.getFileName().toString().equals("module-info.class")) {
                    byte[] data = Files.readAllBytes(p);
                    blackhole(data);
                    // Decompiler d = new Decompiler(data);
                    // ClassInfo ci = d.getClassInfo();
                    // deps.put(ci.getName(), ci);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        SpliteratorMat obj = new SpliteratorMat();
        for (int i = 0; i < 20000; ++i) {
            obj.test();
        }
    }
}
