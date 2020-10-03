# 基础概念

## 基本类型

​	Java 有8大基本类型: `char byte short int long float double boolean`,每一种基本类型都有对应的包装类型. Java会对基本类型和其对应的包装类型进行``自动拆箱和装箱``, 注意,这个过程可能会发生空指针异常.

boolean 很特殊, 他的大小是不确定的, 在 单独使用时是 4字节, 当按照一个数组使用时,大小是 1 字节

Although the Java Virtual Machine defines a `boolean` type, it only provides very limited support for it. There are no Java Virtual Machine instructions solely dedicated to operations on `boolean` values. Instead, expressions in the Java programming language that operate on `boolean` values are compiled to use values of the Java Virtual Machine `int` data type.

The Java Virtual Machine does directly support `boolean` arrays. Its *newarray* instruction ([§*newarray*](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.newarray)) enables creation of `boolean` arrays. Arrays of type `boolean` are accessed and modified using the `byte` array instructions *baload* and *bastore* .

In Oracle’s Java Virtual Machine implementation, `boolean` arrays in the Java programming language are encoded as Java Virtual Machine `byte` arrays, using 8 bits per `boolean` element.

The Java Virtual Machine encodes `boolean` array components using `1` to represent `true` and `0` to represent `false`. Where Java programming language `boolean` values are mapped by compilers to values of Java Virtual Machine type `int`, the compilers must use the same encoding.

## String

​	Java 的String是用 `byte` 数组实现的,之前有版本使用 `char` 数组实现. 为什么要改变这一实现? 因为 char 占用两个字节, 而 byte 只占用一个字节. 

​	一个String对象一旦被创建,则不可以再进行变化. 因为真正的字节数组被声明为 final ,因此 String 是线程安全的.

```Java
private final byte[] value;
```

## StringBuffer

​	StringBuffer 继承了 `AbstractStringBuilder` ,`AbstractStringBuilder` 中定义了一系列的字符串操作方法,在 JDK 15 中,仍然采用的是 byte 数组. 初始大小为 `16`.

​	值得一提的是, StringBuffer 的方法都有 `synchronized`修饰,因此,StringBuffer 是线程安全的

```Java
 public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, Comparable<StringBuffer>, CharSequence
{}
```

## StringBuilder

​	与StringBuffer类似,但是不具有线程安全性.

## 接口与抽象类

​	接口是对行为的抽象,而抽象类是对属性的抽象.

​	接口的限制比抽象类多

## ==与equals

​	对于基本类型, == 会判断两个值是否相同, 对于引用类型, == 会判断指向的地址是否相同.

​	equals 是一个方法,根据需要进行判断

## 为什么重写 equals 需要重写 hashcode

​	hashcode的使用大都集中在 hashmap 相关的数据结构中. 以HashMap的插入过程为例, 首先会来一个与操作`(n - 1) & hash])`, 两个物理地址不同的对象即使逻辑上是相同的, 他们 hash 出来的地址也有可能不同, 这样就会导致存入了重复的值. 因此在重写 equals 时必须重写 hashcode . 

## 深拷贝和浅拷贝

浅拷贝对于基本类型会复制一份值,而引用类型只会复制一个引用,这样在引用的对象变化之后就会出现错误.

深拷贝则是创建新的对象,原有的对象不再影响新的引用.

可以用 `序列化` 的方式实现深拷贝, 参考设计模式中的 `克隆模式` 或者`原型模式`