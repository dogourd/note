package icu.cucurbit.note.examples.bytebuddy.supercall;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * <h1> TargetEnhance </h1>
 *
 * @author yuwen
 * @since 2022-12-26
 */
public class TargetEnhance {

    //@RuntimeType
    //public Object intercept(@SuperCall Callable<?> zuperCall) {
    //    System.err.println("Do In TargetEnhance [1]");
    //    Object ret = null;
    //    try {
    //        ret = zuperCall.call();
    //    } catch (Throwable t) {
    //        System.err.println("Failed To Call Target." + t.getMessage());
    //    }
    //    System.err.println("Do In TargetEnhance [2]");
    //
    //    return ret;
    //}

    @RuntimeType
    public Object intercept(@Origin Class<?> clazz, @AllArguments Object[] allArguments, @Origin Method method, @SuperCall Callable<?> zuper) {
        System.err.println(String.format("class: %s, argCount: %d, method: %s.", clazz.getName(), allArguments.length, method.getName()));

        Object ret = null;
        System.err.println("before enhance.");
        try {
            ret = zuper.call();
        } catch (Throwable t) {
            System.err.println("an error occurred in enhancer.");
        }

        System.err.println("after enhance.");
        return ret;
    }
}
