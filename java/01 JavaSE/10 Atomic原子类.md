## 介绍一下 Atomic 原子类
Atomic 是指一个操作是不可中断的。即使是在多个线程一起执行的时候，一个操作一旦开始，就不会被 其他线程干扰。

## JUC 包中的原子类是哪4类
1. 基本数据类型： AtomicInteger ： 整型原子类  AtomicLong： 长整型原子类    AtomicBoolean： 布尔型原子类
2. 数组类型： AtomicIntegerArray：整型数组原子类  AtomicLongArray：长整型数组原子类  AtomicReferenceArray：引用类型数组原子类
3. 引用类型  AtomicReference： 引用类型原子类 AtomicStampedReference： 原子更新带有版本号的引用类型。该类将整型数值与引用关联起 来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
AtomicMarkableReference： 原子更新带有标记位的引用类型。对象属性修改类型 AtomicIntegerFieldUpdater： 原子更新整型字段的更新器 AtomicLongFieldUpdater： 原子更新长整型字段的更新器
AtomicMarkableReference： 原子更新带有标记位的引用类型

## 简单介绍一下 AtomicInteger 类的原理
AtomicInteger 类主要利用 CAS和 volatile 和 native 方法来保证原子操作，从而避免 synchronized 的 高开销，执行效率大为提升。
```java
// 更新操作时提供“比较并替换”的作用 private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long valueOffset;
static { try{
valueOffset =
unsafe.objectFieldOffset(AutomicInteger.class.getDeclaredField("value")); }catch(Exception ex){ throw new Error(ex);
}
}
private volatile int value;
```
