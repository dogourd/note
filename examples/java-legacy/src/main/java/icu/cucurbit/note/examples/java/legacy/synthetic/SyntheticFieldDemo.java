package icu.cucurbit.note.examples.java.legacy.synthetic;

import java.lang.reflect.Field;

/**
 * <h1> SyntheticFieldDemo </h1>
 *
 * @author yuwen
 * @since 2023-03-15
 */
public class SyntheticFieldDemo {

    class NestedClass {}


    public static void main(String[] args) {
        Field[] fields = SyntheticFieldDemo.NestedClass.class
                .getDeclaredFields();

        for (Field f : fields) {
            // Field: this$0, isSynthetic: true.
            System.out.println("Field: " + f.getName() + ", isSynthetic: " +
                    f.isSynthetic());
        }
    }
}
