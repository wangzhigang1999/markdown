# 基础

> 插值表达式

```java
<body>
<div id="app" class="app">
   {{"你好" + message }}
</div>
</body>
```

> v-on

绑定点击事件,可以使用 @ 替代

```html
<h1 v-text="message" @click="test"></h1>
```

```javascript
methods: {
    test:function() {
        console.log("hello")
    }
}
```

- vue不需要操作dom元素

  ```html
  <body>
  <div id="app" class="app">
      <h1 v-text="message" @click="change"></h1>
  </div>
  </body>
  <script>
      var app = new Vue({
          el: '.app',
          data: {
              message: 'Hello java!'
          },
          methods: {
              change:function() {
                  this.message="hello C++"
              }
          }
      })
  </script>
  ```



> v-html

嵌入html代码

> v-show

- 通过表达式获取值

```html
<h1 @click="show">点击切换遮罩层</h1>
<div v-show="isShow">
    <h1>遮罩层</h1>
</div>

```

```html
show: function () {
    this.isShow = !this.isShow;
}
```

- 也支持判断的方式

```html
<div v-show="isShow==true">
    <h1>遮罩层</h1>
</div>
```

> v-bind

- v-bind可以省略,直接使用   ==:属性名== 即可

- 设置元素的属性

```html
<div id="app" class="app">
    <img v-bind:src="image">
</div>

<script>
    var app = new Vue({
        el: '.app',
        data: {
            image: 'https://cn.vuejs.org/images/logo.png'
        }
    })
</script>
```

> 计算属性

- 类似函数,但是有差异
- 是一个属性,但是会绑定到某些事件

```html
<h2>{{test}}</h2>
```

```JavaScript
computed: {
    test: function () {
        return this.a * this.a;
    }
}
```

# 组件

- 定义一个组件

```javascript
Vue.component("hello", {
    template: "<p>hello</p>"
});
```

- 在html中引用

```html
<hello></hello>
```

- 模板里只能有一个根标签 

# 脚手架

- 模板
- 行为
- 样式
