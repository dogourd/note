package icu.cucurbit.note.examples.java.legacy.synthetic;

import java.lang.reflect.Method;

/**
 * <h1> SyntheticMethodDemo </h1>
 *
 * @author yuwen
 * @since 2023-03-15
 */
public class SyntheticMethodDemo {

    /**
     * java8 - javap
     * class icu.cucurbit.note.examples.java.legacy.synthetic.SyntheticMethodDemo$NestedClass {
     *   private java.lang.String nestedField;
     *
     *   final icu.cucurbit.note.examples.java.legacy.synthetic.SyntheticMethodDemo this$0;
     *
     *   icu.cucurbit.note.examples.java.legacy.synthetic.SyntheticMethodDemo$NestedClass(icu.cucurbit.note.examples.java.legacy.synthetic.SyntheticMethodDemo);
     *     Code:
     *        0: aload_0
     *        1: aload_1
     *        2: putfield      #2                  // Field this$0:Licu/cucurbit/note/examples/java/legacy/synthetic/SyntheticMethodDemo;
     *        5: aload_0
     *        6: invokespecial #3                  // Method java/lang/Object."<init>":()V
     *        9: return
     *
     *   static java.lang.String access$000(icu.cucurbit.note.examples.java.legacy.synthetic.SyntheticMethodDemo$NestedClass);
     *     Code:
     *        0: aload_0
     *        1: getfield      #1                  // Field nestedField:Ljava/lang/String;
     *        4: areturn
     *
     *   static java.lang.String access$002(icu.cucurbit.note.examples.java.legacy.synthetic.SyntheticMethodDemo$NestedClass, java.lang.String);
     *     Code:
     *        0: aload_0
     *        1: aload_1
     *        2: dup_x1
     *        3: putfield      #1                  // Field nestedField:Ljava/lang/String;
     *        6: areturn
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
        Method[] methods = SyntheticMethodDemo.NestedClass.class
                .getDeclaredMethods();

        for (Method m : methods) {
            // Method: access$000, isSynthetic: true
            // Method: access$002, isSynthetic: true
            System.out.println("Method: " + m.getName() + ", isSynthetic: " +
                    m.isSynthetic());
        }
    }
}
