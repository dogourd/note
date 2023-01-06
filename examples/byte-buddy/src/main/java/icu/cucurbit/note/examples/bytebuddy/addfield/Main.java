package icu.cucurbit.note.examples.bytebuddy.addfield;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

/**
 * <h1> Main </h1>
 *
 * @author yuwen
 * @since 2023-01-06
 * */
public class Main {

    private static final String CONTEXT_ATTR_NAME = "$sample";

    public static void main(String[] args) throws Exception {
        //DynamicType.Builder<?> builder = new ByteBuddy().redefine(Base.class).name("icu.cucurbit.note.examples.bytebuddy.addfield.Qux");

        ClassFileLocator locator = ClassFileLocator.ForClassLoader.of(Thread.currentThread().getContextClassLoader());
        TypePool typePool = TypePool.Default.of(locator);
        TypeDescription typeDescription = typePool.describe("icu.cucurbit.note.examples.bytebuddy.addfield.Base").resolve();
        DynamicType.Builder<?> builder = new ByteBuddy().rebase(typeDescription, locator);

        ElementMatcher<MethodDescription> matcher = ElementMatchers
                .not(ElementMatchers.<MethodDescription>isStatic().or(ElementMatchers.isAbstract()).or(ElementMatchers.<MethodDescription>isDeclaredBy(Object.class)))
                .and(ElementMatchers.<MethodDescription>nameMatches("[a-zA-Z0-9_$]+"));



        builder = builder
                .defineField(CONTEXT_ATTR_NAME, Object.class, Opcodes.ACC_PRIVATE | Opcodes.ACC_VOLATILE)
                .implement(EnhancedInstance.class)
                .intercept(FieldAccessor.ofField(CONTEXT_ATTR_NAME));

        builder = builder
                .method(matcher)
                .intercept(MethodDelegation.to(new InstMethodsInter()));

        //builder.make().saveIn(new File("."));

        Class<?> enhanced = builder.make().load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST).getLoaded();

        Object instance = enhanced.getConstructor().newInstance();
        EnhancedInstance enhancedInstance = (EnhancedInstance) instance;

        Object value = new Object();
        enhancedInstance.setField(value);
        System.out.println(enhancedInstance.getField() == value);

        instance.getClass().getDeclaredMethod("abc").invoke(instance);
    }

    public interface EnhancedInstance {

        Object getField();

        void setField(Object value);
    }


    public static class InstMethodsInter {

        public void printABC() {
            System.out.println("abc");
        }
    }
}
