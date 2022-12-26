package icu.cucurbit.note.examples.bytebuddy.supercall;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.pool.TypePool;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * <h1> Main </h1>
 *
 * @author yuwen
 * @since 2022-12-26
 */
public class Main {

    public static void main(String[] args) throws Throwable {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ByteBuddy byteBuddy = new ByteBuddy();
        TypePool pool = TypePool.Default.of(classLoader);
        ClassFileLocator locator = ClassFileLocator.ForClassLoader.of(classLoader);

        String target = "icu.cucurbit.note.examples.bytebuddy.supercall.Target";
        TypeDescription targetDescription = pool.describe(target).resolve();

        //DynamicType.Builder<?> builder = AgentBuilder.TypeStrategy.Default.REDEFINE.builder(
        //        targetDescription, byteBuddy, locator, MethodNameTransformer.Suffixing.withRandomSuffix(),
        //        classLoader, null, Target.class.getProtectionDomain()
        //);

        //DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<Object> builder = byteBuddy.with(InstrumentedType.Factory.Default.FROZEN)
        //        .with(VisibilityBridgeStrategy.Default.NEVER)
        //        .redefine(targetDescription, locator)
        //        .ignoreAlso(LatentMatcher.ForSelfDeclaredMethod.NOT_DECLARED)
        //        .name("icu.cucurbit.note.examples.bytebuddy.supercall.RedefinedTarget")
        //        .method(isStatic().and(named("doSomething")))
        //        .intercept(MethodDelegation.withDefaultConfiguration().to(new TargetEnhance()));

        DynamicType.Builder<?> builder = byteBuddy.rebase(targetDescription, locator)
                .name("icu.cucurbit.note.examples.bytebuddy.supercall.RedefinedTarget")
                .method(isStatic().and(named("doSomething")))
                .intercept(MethodDelegation.withDefaultConfiguration().to(new TargetEnhance()));

        // dump
        //builder.make(pool).saveIn(new File("."));

        Class<?> enhancedClass = builder.make(pool).load(classLoader).getLoaded();
        Method method = enhancedClass.getMethod("doSomething");

        method.invoke(null);
    }
}
