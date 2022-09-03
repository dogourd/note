### 使用ByteBuddy生成wrapper类 (如: `HttpServletRequestWrapper` 之于 `HttpServletRequest`)  
如下为`Foo` 生成wrapper类: `FooWrapper`  
```java
public class Main {
    public static class Foo {
        public String foo1() {
            System.out.println("foo1");
            return "foo1";
        }

        public String foo2() {
            System.out.println("foo2");
            return "foo2";
        }

        public String foo3() {
            System.out.println("foo3");
            return "foo3";
        }
    }
    
    private static Class<? extends Foo> generateWrapperClz() {
        return new ByteBuddy()
                .subclass(Foo.class)
                .name("org.example.problems.bytebuddy.FooWrapper")
                .defineField("delegate", Foo.class, Modifier.PROTECTED)
                .defineConstructor(Modifier.PUBLIC).withParameters(Foo.class)
                .intercept(MethodCall.invoke(Foo.class.getDeclaredConstructor()).andThen(FieldAccessor.ofField("delegate").setsArgumentAt(0)))
                .method(isDeclaredBy(Foo.class).and(not(isConstructor()).and(not(isAbstract())).and(not(isPrivate()))))
                .intercept(MethodCall.invokeSuper().andThen(MethodDelegation.toField("delegate")))
                .make()
                .load(Foo.class.getClassLoader())
                .getLoaded();
    }
}
```
生成 wrapper 类反编译如下:  
```java
package org.example.problems.bytebuddy;

public class FooWrapper extends Main.Foo {
    protected Main.Foo delegate;

    public String foo1() {
        super.foo1();
        return this.delegate.foo1();
    }

    public String foo2() {
        super.foo2();
        return this.delegate.foo2();
    }

    public String foo3() {
        super.foo3();
        return this.delegate.foo3();
    }

    public FooWrapper(Main.Foo var1) {
        this.delegate = var1;
    }

    public FooWrapper() {
    }
}

```