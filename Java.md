### Docker 中使用 gdb

1. 启动参数添加 `--privileged=true` 获取真实root权限, `--cap-add=SYS_PTRACE` 开启ptrace若已添加privileged则不必须。
2. 进入容器后 `ulimit -a` 检查coredump限制，必要可调整为 unlimited。

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