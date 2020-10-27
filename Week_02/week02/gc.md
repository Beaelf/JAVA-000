## gc
gc 都是在内存快满的时候触发，一般而言，首先会是年轻代，当年轻代快满时，会触发Young GC

年轻代放不下了的对象，新生代达到阈值的对象都会往老年代方，当老年代也快放不下了，就会触发
Old GC。如果gc后还是放不下，则会发生OOM异常

## gc 分析
* java -Xmx128m  -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis

* java -Xmx128m  -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis

分别使用穿行GC和并行GC，两者都会触发stw事件，但是并行gc是一个多线程的模式，
对cpu的利用更优。在同样的事件里能清理更多的垃圾

* java -Xmx128m  -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis
cms

cms gc 分阶段并发处理，在cms中会有三次标记，初始标记跟最终标记会触发stw事件，但是在
串行/并发gc中只有一次。而在性能上cms会更好，因为cms初始标记至标记与GC Root有
直接引用关系的对象，不区分年轻代或老年代，然后第二次，并发标记，到此为止，基本上
标记了绝大部分需要清理的对象，因此初始标记跟最终标记会触发stw事件并不会很耗时。

