package icu.cucurbit.note.examples.bytebuddy.supercall;

/**
 * <h1> Target </h1>
 *
 * @author yuwen
 * @since 2022-12-26
 */
public class Target {

    public static Object doSomething() {
        System.err.println("Do In Target.");
        return new Object();
    }





}
