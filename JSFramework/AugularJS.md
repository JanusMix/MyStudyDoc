#AugularJS
什么是AugularJS？简单来说就是一个JS的框架，它与jQuery不同，jQuery是一个工具，可以方便的操作很多事情，AugularJS是一个蓝图，可以为项目做规划的框架。

##Hello World
一切语言都是以Hello World开始，AugularJS原生也是JS，所以看起来还是比较好理解的。

```html
<!doctype html>
<html ng-app>
    <head>
        <script src="http://code.angularjs.org/angular-1.0.1.min.js"></script>
    </head>
    <body>
        Hello {{'World'}}!
    </body>
</html>
```

执行此html文件就可以看到永远的Hello World。现在来观察上面与原本的html有何异同：

 - html标签后面有个`ng-app`标记，它是告诉AugularJS这个html需要处理和引导应用了，也可以在其它标签加入此标记说明仅在此标记内运行脚本
 - script这一行用来载入AugularJS脚本，和jQuery一样
 - 标签的正文是应用的模板，模板是什么下面会有介绍，使用双大括号{{}}标记的内容是问候语中绑定的表达式，此处这个表达式是一个简单的字符串
 
下面使用AugularJS的双向数据绑定为我们的Hello World绑定一个动态的表达式

```html
<!doctype html>
<html ng-app>
    <head>
        <script src="http://code.angularjs.org/angular-1.0.1.min.js"></script>
    </head>
    <body>
        Your name: <input type="text" ng-model="yourname" placeholder="World">
        <hr>
        Hello {{yourname || 'World'}}!
    </body>
</html>
```

又出现了几个没见过的东西，我们慢慢来看：

 - 文本输入指令`<input ng-model="yourname" />`绑定到一个叫`yourname`的模型变量
 - 双大括号标记将`yourname`模型变量添加到问候语文本
 - 你不需要为该应用另外注册一个事件侦听器或添加事件处理程序
 
刷新浏览器，会看到下面的页面：

![HelloWorldAdd](../img/ajs_hello_world_add.png)

一开始页面有输入框和Hello World，而且输入框中默认有world的非文本标记对应上面的`placeholder`，如果往输入框输入某些东西，会把Hello World！中的World即时同步为输入内容，这就是双向绑定的概念。







**工作之余会持续更新**
