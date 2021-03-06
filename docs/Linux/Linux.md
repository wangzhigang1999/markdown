# Linux

## 命令

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

