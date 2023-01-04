### Docker 中使用 gdb

1. 启动参数添加 `--privileged=true` 获取真实root权限, `--cap-add=SYS_PTRACE` 开启ptrace若已添加privileged则不必须。
2. 进入容器后 `ulimit -a` 检查coredump限制，必要可调整为 unlimited。

### Linux CoreDump
1. 查看 core dump 导出路径及文件名信息: `cat /proc/sys/kernel/core_pattern`; 
2. 临时修改路径及文件名信息可直接编辑 `/proc/sys/kernel/core_pattern` 文件, 永久修改须使用 `sysctl -w kernel.core_pattern=custom`

### 记录 JVM 类编译信息

`-XX:+PrintCompilation`，启动性能影响非常严重，如必要可使用。

### 记录 JVM 类 redefine 信息

`-XX:TraceRedefineClasses=3174407`    
1+2+4+4096+8192+16384+1048576+2097152 == 3174407  
HostSpot OpenJDK(jdk/test/java/lang/instrument/RedefineSubclassWithTwoInterfaces.sh)  
该参数支持的各个值说明:    
|十六进制|十进制|作用|
|:----:|:----:|:----|
|0x00000001 | 1| name each target class before loading, after loading and after redefinition is completed|
|0x00000002 | 2| print info if parsing, linking or verification throws an exception|
|0x00000004 | 4| print timer info for the VM operation|
|0x00001000 | 4096| detect calls to obsolete methods|
|0x00002000 | 8192| fail a guarantee() in addition to detection|
|0x00004000 | 16384| detect old/obsolete methods in metadata|
|0x00100000 | 1048576| impl details: vtable updates|
|0x00200000 | 2097152| impl details: itable updates|

### 记录 JVM 类装载卸载记录
`-XX:+TraceClassLoading`, `-XX:+TraceClassUnloading`

### 禁止省略异常堆栈跟踪
HotSpot VM 中, JVM会记住异常调用栈信息(**StackTrace**), 当某个异常经常发生时, 调用栈信息将不会再打印, 以此来实现更好的性能,
HotSpot 默认开启该功能, 可能导致部分异常调用栈信息无法获取。 例如在调用`NullPointerException.printStackTrace()` 时
输出内容为:
```text
开启该功能时
java.lang.NullPointerException

禁用该功能时
java.lang.NullPointerException
  at a.b.c(b.java:123)
  at a.b.b(b.java:122)
``` 
可使用 `-XX:-OmitStackTraceInFastThrow` 选项禁用该功能.