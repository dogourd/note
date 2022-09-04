package icu.cucurbit.note.examples.bytebuddy.genwrapper;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;

import java.lang.reflect.Method;

import static net.bytebuddy.implementation.MethodCall.invoke;
import static net.bytebuddy.implementation.MethodDelegation.toField;
import static net.bytebuddy.matcher.ElementMatchers.isAbstract;
import static net.bytebuddy.matcher.ElementMatchers.isConstructor;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.isPublic;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class Simple {


    /**
     * 要求 original 不被 final 修饰，同时需要具备无参构造器.
     * */
    public static <T> Class<? extends T> subWrapperFor(Class<T> original) throws Throwable {
        final String DELEGATE_NAME = "delegate";

        DynamicType.Unloaded<T> unloaded = new ByteBuddy()
                .subclass(original)
                .name(original.getName() + "SubClassWrapper")
                .defineField(DELEGATE_NAME, original, Visibility.PROTECTED)
                .defineConstructor(Visibility.PUBLIC).withParameters(original)
                .intercept(invoke(original.getDeclaredConstructor()).andThen(FieldAccessor.ofField(DELEGATE_NAME).setsArgumentAt(0)))
                .method(isPublic().and(isDeclaredBy(original)).and(not(isConstructor())).and(not(isAbstract())))
                .intercept(toField(DELEGATE_NAME))
                .make();
        //unloaded.saveIn(new File("."));
        return unloaded
                .load(original.getClassLoader())
                .getLoaded();
    }

    /**
     * 要求 original 父类具备无参构造器.
     * */
    public static <T> Class<?> wrapperFor(Class<T> original) throws Throwable {
        final String DELEGATE_NAME = "delegate";

        DynamicType.Loaded<T> loaded = new ByteBuddy()
                .redefine(original)
                .name(original.getName() + "RedefineWrapper")
                .defineField(DELEGATE_NAME, original, Visibility.PROTECTED)
                .defineConstructor(Visibility.PUBLIC).withParameters(original)
                .intercept(invoke(original.getSuperclass().getDeclaredConstructor()).andThen(FieldAccessor.ofField(DELEGATE_NAME).setsArgumentAt(0)))
                .method(isPublic().and(isDeclaredBy(original)).and(not(isConstructor())).and(not(isAbstract())))
                .intercept(toField(DELEGATE_NAME))
                .make()
                .load(original.getClassLoader());
        //loaded.saveIn(new File("./dumps"));
        return loaded.getLoaded();
    }



    public static void testRedefine() throws Throwable {
        // redefine.
        Class<?> wrapperClz = wrapperFor(Foo.class);
        System.out.println("wrapperClz: " + wrapperClz);
        System.out.println("wrapperPkg: " + wrapperClz.getPackageName());

        Foo foo = new Foo();
        Object wrapper = wrapperClz.getConstructor(Foo.class).newInstance(foo);

        for (Method method : wrapperClz.getDeclaredMethods()) {
            if (!method.canAccess(wrapper)) {
                continue;
            }
            method.invoke(wrapper);
        }
    }

    public static void testSubClass() throws Throwable {
        Class<? extends Foo> wrapperClz = subWrapperFor(Foo.class);
        System.out.println("sub wrapperClz: " + wrapperClz);
        System.out.println("sub wrapperPkg: " + wrapperClz.getPackageName());

        Foo foo = new Foo();
        Foo wrapper = wrapperClz.getConstructor(Foo.class).newInstance(foo);

        wrapper.foo1();
        wrapper.foo2();
    }


    public static void main(String[] args) throws Throwable {

        System.out.println("test redefine");
        testRedefine();

        System.out.println();
        System.out.println("test subclass");
        testSubClass();
    }

}
