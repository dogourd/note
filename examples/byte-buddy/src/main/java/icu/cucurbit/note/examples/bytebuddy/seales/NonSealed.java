package icu.cucurbit.note.examples.bytebuddy.seales;

/**
 * <h1> NonSealed </h1>
 *
 * @author yuwen
 * @since 2022-12-20
 */
public non-sealed class NonSealed extends Sealed {

    public static class B extends NonSealed {

    }

    public static class C extends B {

    }
}
