# NGINX

## 什么是Nginx

**轻量级Web服务器**，它不仅是一个**高性能的HTTP和反向代理服务器**，同时也是一个IMAP/POP3/SMTP 代理服务器。

## 负载均衡

![image-20200430175145448](NGINX.assets/image-20200430175145448.png)

> 轮询策略

```json
# 自定义名称不要加下划线
upstream myserver{
    server 39.107.77.0;
    server 61.135.169.121;
}
server {
        listen       80;
        server_name  localhost;

        location / {
            proxy_pass  http://myserver/;
        }

    }
```



> 权重策略

```json
upstream myserver{
    # 加入权重即可
    server 39.107.77.0 weight=10;
    server 61.135.169.121:443 weight =2;
}
server {
        listen       80;
        server_name  localhost;

        location / {
            proxy_pass http://myserver/;  
        }

    }
```



> IP HASH

```json
upstream myserver{
    ip_hash;
    server 39.107.77.0;
    server 61.135.169.121:443;
}
server {
        listen       80;
        server_name  localhost;
        location / {
            proxy_pass http://myserver/;
        }
    }
```



## 反向代理

> 正向代理

- 代理客户端了解客户和服务器都是谁
- 突破访问权限
- 提高访问速度
- 隐藏客户的ip

> 反向代理

- 反向代理服务器,将请求进行转发.
- 配置在服务端
- 客户端不知道访问的究竟是哪一台服务器
- 隐藏服务器真正的地址

![image-20200430174722991](NGINX.assets/image-20200430174722991.png)

```json
server {
        listen       80;
        server_name  localhost;
    
		# 请求转发
        location / {
            proxy_pass  https://www.baidu.com/;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
     }
 }
```

> location的映射规则

- 精准匹配, 主机后边不能带任何字符串

```json
location = / {
}
```

- 通用匹配, 匹配所有的以/XXX开头的路径

```json
location / XXX{
    
}
```

- 正则匹配, 与通用匹配类似,但是优先级要高

```json
location ~/XXX{
}
```

- 匹配开头,匹配所有以 /XXX 开头的路径,优先级更高

```json
location ^~/XXX{
}
```

- 匹配结尾

```json
location ~*\.(png|gif)${
    
}
```





## 动静分离

![image-20200430175513931](NGINX.assets/image-20200430175513931.png)

## 配置文件

```nginx
# 全局块

# nginx 处理的并发量
worker_processes  1;
# events 块
events {
    # nginx 支持的最大连接数
    worker_connections  1024;
}
#http 块
http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    server {
        listen       80;
        server_name  localhost;

        location / {
            root   html;
            index  index.html index.htm;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

> Linux 下的 nginx 配置文件

```json
user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;
# 上边都是全局块

events {
    # 数值越大,并发能力越强
    worker_connections  1024;
}

# http 块
http {
    # 引入一个外部的文件, 包含一些媒体类型
    include       /etc/nginx/mime.types;
    # 默认的类型
    default_type  application/octet-stream;

    # 引入了conf文件, 在conf.d里所有的以 .conf 为后缀的文件
    #include /etc/nginx/conf.d/*.conf;
    # 下边是 default.conf 里的详细配置
    
   		# server 块, 主要修改的
        server {
    	# 监听的端口
        listen       80;
        listen  [::]:80;
		 # 接受请求的ip
        server_name  localhost;
		# location
        location / {
            # root 表示将请求根据 /user/share/nginx/html 去查找静态资源
            root   /usr/share/nginx/html;
            # index 表示默认去上述文件目录找 index.html 或者 index.htm
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   /usr/share/nginx/html;
        }
    }
}
```

## 集群搭建





