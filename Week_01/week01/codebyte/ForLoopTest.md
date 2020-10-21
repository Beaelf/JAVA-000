```
0: new           #2                  // class com/megetood/geek/week01/codebyte/MovingAverage
       3: dup
       4: invokespecial #3                  // Method com/megetood/geek/week01/codebyte/MovingAverage."<init>":()V
       7: astore_1
    // 获取数组
       8: getstatic     #4                  // Field numbers:[I
    // 保存局部对象
      11: astore_2
    // 加载刚刚保存的
      12: aload_2
    // 将数组长度压栈
      13: arraylength
    // 保存数组长度值到局部变量表3号位置
      14: istore_3
    // 循环计数器，i
      15: iconst_0
      16: istore        4
    // 获取计数器初始值压栈
      18: iload         4
    // 将数组长度压栈
      20: iload_3
    // 比较计数器值与数组长度，如果计数器值>=数组长度，则跳到中间的指令，到43行开始执行
      21: if_icmpge     43
    // 下面是ma.submit(number);这条指令的字节码
      24: aload_2
      25: iload         4
      27: iaload
      28: istore        5
      30: aload_1
      31: iload         5
      33: i2d
      34: invokevirtual #5                  // Method com/megetood/geek/week01/codebyte/MovingAverage.submit:(D)V
    // 计数器自增
      37: iinc          4, 1
    // 跳到18行继续执行
      40: goto          18
    // 下面是double avg = ma.getAvg();这条指令的字节码
      43: aload_1
      44: invokevirtual #6                  // Method com/megetood/geek/week01/codebyte/MovingAverage.getAvg:()D
      47: dstore_2
      48: return
    // 这里就是局部变量表了
 	LocalVariableTable:
        Start  Length  Slot  Name   Signature
           30       7     5 number   I
            0      49     0  args   [Ljava/lang/String;
            8      41     1    ma   Lcom/megetood/geek/week01/codebyte/MovingAverage;
           48       1     2   avg   D

```