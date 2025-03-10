Java 8 引入的特性，本质是匿名函数，可作为参数传递给方法或赋值给变量，用于实现只有一个抽象方法的函数式接口

1. 简化集合操作：与 Stream API 结合，进行过滤、映射、排序等操作，如 list.stream().filter(s -> s.startsWith("a"))
2. 简化线程创建：在 java.util.concurrent 包中，提交 Callable 或 Runnable 任务时可用，如 new Thread(() -> System.out.println("Running in a new thread.")).start()。
3. 简化事件监听：在某些框架或库中，可方便定义事件处理逻辑。
4. 简化排序逻辑：定义 Comparator 时使用，如 list.sort((a, b) -> a.compareTo(b)


在 Java 中，Stream 是 Java 8 引入的一个新的抽象概念，它允许你以声明式的方式处理数据集合，提供了一种高效且易于使用的方式来处理集合中的元素。Stream 操作主要分为中间操作和终端操作，以下为你详细介绍：

通过使用中间操作生成一个新的集合，简介高效的处理集合中的数据
