# 集合

![ArrayList](Collections.assets/ArrayList.svg)

## ArrayList

### 创建

ArrayList 的底层是使用 Object 数组实现的, 在创建的时候,会将 elementData 数组 指向一个空的对象数组.

```Java
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```

如果指定了默认的大小,则会创建一个新的对象数组:

```Java
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } 
}
```

### 扩容

```Java
private Object[] grow() {
    return grow(size + 1);
}
```

如果是第一次扩容,则会初始化大小为 10, 如果不是,则转去 `newLength` 方法去获得一个新的长度

```Java
private Object[] grow(int minCapacity) {
    int oldCapacity = elementData.length;
    if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        int newCapacity = ArraysSupport.newLength(oldCapacity,
                minCapacity - oldCapacity, /* minimum growth */
                oldCapacity >> 1           /* preferred growth */);
        return elementData = Arrays.copyOf(elementData, newCapacity);
    } else {
        // 初次添加元素,初始化为大小为 10 
        return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
    }
}
```

总结一些下边的方法:

​	绝大部分情况下,都会扩容到原来大小的2倍.

​	如果扩容后大小不超过 `MAX_ARRAY_LENGTH = Integer.Max-8`, 返回扩容后大大小`Math.max(minGrowth, prefGrowth) + oldLength;`

​	如果超过了,则再次进行判断: 如果扩容后的大小为负数,说明溢出了,则申请的空间太大直接抛出OOM异常.

若仍在`MAX_ARRAY_LENGTH`范围内,返回`MAX_ARRAY_LENGTH`,否则返回 `Integer.MAX_VALUE`.

```Java
public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
	// old = elementData.length
    // min = (minCapacity - oldCapacity)  就是当前数组中剩余的那些位置的个数
    // pref = elementData.length*2
    int newLength = Math.max(minGrowth, prefGrowth) + oldLength;
    if (newLength - MAX_ARRAY_LENGTH <= 0) {
        return newLength;
    }
    return hugeLength(oldLength, minGrowth);
}
```

```Java
// 处理大的长度的函数
private static int hugeLength(int oldLength, int minGrowth) {
    int minLength = oldLength + minGrowth;
    if (minLength < 0) { // overflow
        throw new OutOfMemoryError("Required array length too large");
    }
    if (minLength <= MAX_ARRAY_LENGTH) {
        return MAX_ARRAY_LENGTH;
    }
    return Integer.MAX_VALUE;
}
```

### RandomAccess

这是一个简单的接口,什么都没有定义. 只是表明 ArrayList 支持随机访问. LinkedList 就没有实现这个接口.

```Java
public interface RandomAccess {
}
```

### ArrayList 与 Vector 的区别

Vector 创建的时候会默认初始大小为0

扩容的时候有一个 `capacityIncrement` 变量,扩容的时候如果`capacityIncrement`不为零,则增长 `capacityIncrement`,否则``二倍增长``.

Vector 是线程安全的,方法都增加了 `synchronized`关键字修饰

## LinkedList

![LinkedList](Collections.assets/LinkedList.svg)

### 创建

事实上,在 new 出一个对象时什么都没有做

```Java
public LinkedList() {
}
```

### 增加

先封装一个双链表的节点,然后将它连到链表上即可

```Java
Node(Node<E> prev, E element, Node<E> next) {
    this.item = element;
    this.next = next;
    this.prev = prev;
}
```

```Java
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

## HashMap

![HashMap](Collections.assets/HashMap.svg)

`最大capacity`= 1<<30

### Node节点

```Java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

	// .........
    
    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
}
```

### 创建

在创建HashMap的时候,默认的负载因子是 16 . 负载因子的含义是当  占用的空间/总空间>=负载因子时,进行扩容操作

```Java
/**
 * Constructs an empty {@code HashMap} with the default initial capacity
 * (16) and the default load factor (0.75).
 */
public HashMap() {
    // 默认的负载因子 0.75 ,默认的容量是 16
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}
```

在创建的时候如果指定了初始的容量大小, Java不会直接按照给定的容量进行初始化,而是进行一个神奇的操作:

```Java
static final int tableSizeFor(int cap) {
    int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

将这个函数的返回值作为真正的阈值,简而言之,这个函数的目的就是为了找到``不小于cap的2的幂次的数``

### 添加

添加的时候使用的是下边这个方法,后边两个参数的值为 `false` 和 `true`

```Java
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict)       
```

这个方法有一点长:

```Java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,  boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 首先判断 map 是否未经初始化,如果未初始化, 调用 resize 方法进行初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // hash 一下,判断对应位置是否已经有值存在,不存在,则新建节点然后放进去即可
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        // key 已经存在了
        Node<K,V> e; K k;
        // 如果两个键相同,更新一下值即可
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果是一个红黑树节点, 调用红黑树的 put 方法
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //在链表上查找
            for (int binCount = 0; ; ++binCount) {
                // 找到了链表的尾巴,将新的key编程节点插入链表尾部
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 判断是否要进行树化,当链表的长度 >= 7 进入 treeifyBin 方法,但是并不一定会真的树化
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                 // 在链表上发现了同样的 key ,直接返回 
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
		//......
    }
}
```

### 扩容

这个方法真是太长了,简要概括: 要么是初始化一个未使用的map,要么就是将大小变为2倍

```Java
/**
 * Initializes or doubles table size.  If null, allocates in
 * accord with initial capacity target held in field threshold.
 * Otherwise, because we are using power-of-two expansion, the
 * elements from each bin must either stay at same index, or move
 * with a power of two offset in the new table.
 *
 * @return the table
 */
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    // 获得旧的 capacity
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    // 获得旧的阈值
    int oldThr = threshold;
    int newCap, newThr = 0;

    // 旧的  capacity>0 说明map不是初始化
    if (oldCap > 0) {
        //旧的capacity已经达到了最大值
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 二倍操作
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    // 只有初始化的时候才会进入这个分支,写代码的鬼才~
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    
    // 计算一下新的阈值
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        
        // 展开了一下三目运算符
        //newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
        //          (int)ft : Integer.MAX_VALUE);
         if (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY) {
            newThr = (int) ft;
        } else {
            newThr = Integer.MAX_VALUE;
        }
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    // 开始进行值的迁移
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

### 底层实现

JDK1.8之前使用的是链表+数组的形式,之后使用的是链表+数组+红黑树的形式

### 扰动函数

其实就是一个求hash的函数,定义 null 的hash值为 0.

当 key 不为null时,将hashcode的高16位和低16位相与

```Java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

### 为什么长度是2的幂次

在进行put的时候,要进行 index = hash%length 来找到插入的位置,如果长度是2的幂次,直接进行与操作即可,能够极大的提高效率

## HashSet

![HashSet](Collections.assets/HashSet.svg)

### 创建

HashSet 是基于HashMap 实现的

```Java
public HashSet() {
    map = new HashMap<>();
}
```

### 检查重复

HashSet 在检查重复的时候会先检查hashcode,如果两个元素的key的hashcode相同,再调用 equals 方法进行判断.

## ConcurrentHashMap

![ConcurrentHashMap](Collections.assets/ConcurrentHashMap.svg)

### 创建

```Java
/**
 * Creates a new, empty map with the default initial table size (16).
 */
public ConcurrentHashMap() {
}
```

### 并发级别



## CopyOnWriteArrayList

