# Linux

## 命令

> who am i

我是谁

> pwd

显示当前目录的路径

> ip a

显示当前的ip

> ls -a | -d | -l

查看一个目录

> cat

将FILE或标准输入连接到标准输出。通常是打开显示文件

> mv

移动文件

> cp

复制文件

> echo

打印,相当于printf

> head

显示文件的前多少行,默认是10行

> tail

显示文件的尾部

> wc

统计文件的行数/字符数

> kill

```bash
kill [-s signal|-p] [-q sigval] [-a] [--] pid...
kill -l [signal]
```

```bash
-l 信号，若果不加信号的编号参数，则使用“-l”参数会列出全部的信号名称

-a 当处理当前进程时，不限制命令名和进程号的对应关系

-p 指定kill 命令只打印相关进程的进程号，而不发送任何信号

-s 指定发送信号

-u 指定用户
```

> grep

