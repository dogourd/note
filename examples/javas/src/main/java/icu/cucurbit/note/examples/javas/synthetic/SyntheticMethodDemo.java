package icu.cucurbit.note.examples.javas.synthetic;

import java.lang.reflect.Method;

/**
 * <h1> SyntheticMethodDemo </h1>
 *
 * @author yuwen
 * @since 2023-03-15
 */
public class SyntheticMethodDemo {

    /**
     * java17 - javap
     * class icu.cucurbit.note.examples.javas.synthetic.SyntheticMethodDemo$NestedClass {
     *   private java.lang.String nestedField;
     *
     *   final icu.cucurbit.note.examples.javas.synthetic.SyntheticMethodDemo this$0;
     *
     *   icu.cucurbit.note.examples.javas.synthetic.SyntheticMethodDemo$NestedClass(icu.cucurbit.note.examples.javas.synthetic.SyntheticMethodDemo);
     *     Code:
     *        0: aload_0
     *        1: aload_1
     *        2: putfield      #1                  // Field this$0:Licu/cucurbit/note/examples/javas/synthetic/SyntheticMethodDemo;
     *        5: aload_0
     *        6: invokespecial #7                  // Method java/lang/Object."<init>":()V
     *        9: return
     * }
     * */
    class NestedClass {
        private String nestedField;
    }

    public String getNestedField() {
        return new NestedClass().nestedField;
    }

    public void setNestedField(String nestedField) {
        new NestedClass().nestedField = nestedField;
    }


    public static void main(String[] args) {
        Method[] methods = NestedClass.class
                .getDeclaredMethods();

        for (Method m : methods) {
            // nothing.
            System.out.println("Method: " + m.getName() + ", isSynthetic: " +
                    m.isSynthetic());
        }
    }
}
