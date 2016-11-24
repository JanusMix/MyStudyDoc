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

通过`ng-app`来自动引导AugularJS是一种简洁的方式适合大多数情况。高级开发中，如利用脚本装载应用，可以使用BootStrap手动引导。

引导过程中有这几个重要点

 - 注入器(injector)将用于创建此应用程序的依赖注入(dependency injection)；
 - 注入器将会创建根作用域作为我们应用模型的范围；
 - AngularJS将会链接根作用域中的DOM，从用ngApp标记的HTML标签开始，逐步处理DOM中指令和绑定。

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

##AugularJS模板
一个应用的代码架构有很多种，AugularJS中用的也是MVC(model-view-controller)的设计模式，观察下面的代码

```html
<!doctype html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<html ng-app>
<head>
  <script src="http://code.angularjs.org/angular-1.0.1.min.js"></script>
  <script language = "JavaScript">
	function PhoneListCtrl($scope) {
		$scope.phones = [
			{"name": "Nexus S","snippet": "Fast just got faster with Nexus S."},
			{"name": "Motorola XOOM™ with Wi-Fi","snippet": "The Next, Next Generation tablet."},
			{"name": "MOTOROLA XOOM™","snippet": "The Next, Next Generation tablet."}
		];
	}
  </script>
</head>
<body ng-controller="PhoneListCtrl">
  <ul>
    <li ng-repeat="phone in phones">
      {{phone.name}}
    <p>{{phone.snippet}}</p>
    </li>
  </ul>
</body>
</html>
```

script中的`PhoneCrtl()`写了一个简单的控制器，虽然它没控制什么东西，但它在这里通过给定我们数据模型的语境，允许我们建立模型和视图之间的数据绑定。我们是这样把表现层，数据和逻辑部件联系在一起的：

 - PhoneListCtrl——控制器方法的名字和`<body>`标签里面的`ngController`的值相匹配
 - 手机的数据此时与注入到我们控制器函数的作用域（$scope）相关联。当应用启动之后，会有一个根作用域被创建出来，而控制器的作用域是根作用域的一个典型后继。这个控制器的作用域对所有`<body ng-controller="PhoneListCtrl">`标记内部的数据绑定有效。
 
一个作用域可以视作模板、模型和控制器协同工作的粘接器。AngularJS使用作用域，同时还有模板中的信息，数据模型和控制器。这些可以帮助模型和视图分离，但是他们两者确实是同步的！任何对于模型的更改都会即时反映在视图上；任何在视图上的更改都会被立刻体现在模型中。

再看下面body中的部分，

 - 在`<li>`标签里面的`ng-repeat="phone in phones"`语句是一个AngularJS迭代器。这个迭代器告诉AngularJS用第一个`<li>`标签作为模板为列表中的每一部手机创建一个`<li>`元素。
 - 这里的表达式实际上是我们应用的一个数据模型引用，这些我们在PhoneListCtrl控制器里面都设置好了。










**工作之余会持续更新**
