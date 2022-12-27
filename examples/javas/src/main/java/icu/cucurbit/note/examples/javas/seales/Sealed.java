package icu.cucurbit.note.examples.javas.seales;

/**
 * <h1> Seales </h1>
 *
 * @author yuwen
 * @since 2022-12-20
 */
public sealed class Sealed permits Sealed.A, NonSealed {

    public static final class A extends Sealed {

    }


}
