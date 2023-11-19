java -ea -XX:-TieredCompilation -XX:+UnlockExperimentalVMOptions -XX:CompileOnly=SubList::test -XX:CompileCommand="dontinline,java.util.ArrayList::add" SubList

