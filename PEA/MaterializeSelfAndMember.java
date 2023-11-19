class MaterializeSelfAndMember {
    // Based on
    // https://github.com/openjdk/jdk/blob/8ff7d6ea0a9615fa4c5229afa5f0df8ad9c4f4f6/src/java.base/share/classes/jdk/internal/org/objectweb/asm/MethodWriter.java#L656-L657

    Member member;

    class Member {
        Object obj;
    }

    MaterializeSelfAndMember() {
        member = new Member();
        // We need to update memory state after materializing `this` and `member`
        // Otherwise, loads of `this.member` will use the original put of this
        // into member as an alias. That put points to the dead instance of `this`.
        initMember(member);
    }

    void initMember(Member member) {
        member.obj = new Object();
    }

    public static void wrapper() {
        MaterializeSelfAndMember m = new MaterializeSelfAndMember();
        assert m.member.obj != null : "Messed up member variable after materialization";
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; ++i) {
            wrapper();
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}
        // Wait some time for the compilation task to complete before triggering the exception
        wrapper();
    }
}
