java -ea -XX:-TieredCompilation -XX:CompileOnly="MaterializeSelfAndMember::wrapper" -XX:CompileCommand="dontinline,MaterializeSelfAndMember::initMember" MaterializeSelfAndMember
