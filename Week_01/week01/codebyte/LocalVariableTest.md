```
public static void main(java.lang.String[]);
    Code:
    // 创建对象
       0: new           #2                  // class com/megetood/geek/week01/codebyte/MovingAverage
    // 赋值栈顶引用值
       3: dup
    // 调用构造函数，初始化
       4: invokespecial #3                  // Method com/megetood/geek/week01/codebyte/MovingAverage."<init>":()V
    // 将新建的对象引用地址值存储到局部变量表索引为1的位置
       7: astore_1
    // 将常量值1压栈
       8: iconst_1
    // 将常量1存储到局部变量表索引为2的位置
       9: istore_2
    // 将常量值2压栈
      10: iconst_2
    // 将常量1存储到局部变量表索引为3的位置
      11: istore_3
    // 将局部变量表索引为1、2的值压栈，分别对应前面新建的MovingAverage对象和int值1
      12: aload_1
      13: iload_2
    // 对int值1进行类型转换，转换成double类型
      14: i2d
    // 调用movingAverage对象的submit方法
      15: invokevirtual #4                  // Method com/megetood/geek/week01/codebyte/MovingAverage.submit:(D)V
    // 这三行跟上面是同样的操作
      18: aload_1
      19: iload_3
      20: i2d
      21: invokevirtual #4                  // Method com/megetood/geek/week01/codebyte/MovingAverage.submit:(D)V
      24: aload_1
      25: invokevirtual #5                  // Method com/megetood/geek/week01/codebyte/MovingAverage.getAvg:()D
    // 将25行函数调用结果存储到局部变量表索引为4的位置
      28: dstore        4
    // 返回栈顶值
      30: return
}
```