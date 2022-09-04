package icu.cucurbit.note.examples.bytebuddy.genwrapper;

public class Foo extends Father implements Interf {

    public String foo1() {
        String v = "foo1";
        System.out.println(v);
        return v;
    }

    public String foo2() {
        String v = "foo2";
        System.out.println(v);
        return v;
    }

    private String _foo3() {
        String v = "foo3";
        System.out.println(v);
        return v;
    }
}
