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
 - 不需要为该应用另外注册一个事件侦听器或添加事件处理程序
 
刷新浏览器，会看到下面的页面：

![Img_01](../img/ajs_hello_world_add.png)

一开始页面有输入框和Hello World，而且输入框中默认有world的非文本标记对应上面的`placeholder`，如果往输入框输入某些东西，会把Hello World！中的World即时同步为输入内容，这就是双向绑定的概念。

##AugularJS模板
一个应用的代码架构有很多种，AugularJS中用的最多的也是MVC(model-view-controller)的设计模式，观察下面的代码

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

body中的部分就是模板。script中的`PhoneListCrtl()`写了一个简单的控制器，虽然它没控制什么东西，但它在这里通过给定我们数据模型的语境，允许我们建立模型和视图之间的数据绑定。我们是这样把表现层，数据和逻辑部件联系在一起的：

 - PhoneListCtrl——控制器方法的名字和`<body>`标签里面的`ngController`的值相匹配
 - 手机的数据此时与注入到我们控制器函数的作用域（$scope）相关联。当应用启动之后，会有一个根作用域被创建出来，而控制器的作用域是根作用域的一个典型后继。这个控制器的作用域对所有`<body ng-controller="PhoneListCtrl">`标记内部的数据绑定有效。
 
一个作用域可以视作模板、模型和控制器协同工作的粘接器。AngularJS使用作用域，同时还有模板中的信息，数据模型和控制器。这些可以帮助模型和视图分离，但是他们两者确实是同步的！任何对于模型的更改都会即时反映在视图上；任何在视图上的更改都会被立刻体现在模型中。

再看下面body中的部分，

 - 在AugularJS中，视图是模型通过HTML模板渲染之后的映射。这意味着，不论模型什么时候发生变化，AngularJS会实时更新结合点，随之更新视图。此处视图组件被AngularJS用body这个模板构建出来
 - 在`<li>`标签里面的`ng-repeat="phone in phones"`语句是一个AngularJS迭代器。这个迭代器告诉AngularJS用第一个`<li>`标签作为模板为列表中的每一部手机创建一个`<li>`元素。
 - 这里的表达式实际上是我们应用的一个数据模型引用，这些我们在`PhoneListCtrl`控制器里面都设置好了。
 
##迭代器过滤
在不修改控制器的情况下我们可以完成一个全文搜索功能。在模板中加入一下内容：

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
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span2">
      <!--Sidebar content-->
      Search: <input ng-model="query">
    </div>
    <div class="span10">
      <!--Body content-->
      <ul class="phones">
        <li ng-repeat="phone in phones | filter:query">
          {{phone.name}}
        <p>{{phone.snippet}}</p>
        </li>
      </ul>
    </div>
  </div>
</div>
</body>
</html>
```

我们现在添加了一个`<input>`标签，并且使用AngularJS的`$filter`函数来处理`ngRepeat`指令的输入。这样可以在用户在输入框输入内容时，即时匹配内容显示。新增的内容解释如下：

 - 数据绑定： 这是AngularJS的一个核心特性。当页面加载的时候，AngularJS会根据输入框的属性值名字，将其与数据模型中相同名字的变量绑定在一起，以确保两者的同步性。在这段代码中，用户在输入框中输入的数据名字称作`query`，会立刻作为列表迭代器（`phone in phones | filter:query`）其过滤器的输入。当数据模型引起迭代器输入变化的时候，迭代器可以高效得更新DOM将数据模型最新的状态反映出来。
 - 使用`filte`r过滤器：`filter`函数使用`query`的值来创建一个只包含匹配`query`记录的新数组。`ngRepeat`会根据`filter`过滤器生成的手机记录数据数组来自动更新视图。整个过程对于开发者来说都是透明的。

##双向绑定
观察下面代码：

```html
<!doctype html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<html ng-app>
<head>
  <script src="http://code.angularjs.org/angular-1.0.1.min.js"></script>
  <script language = "JavaScript">
	function PhoneListCtrl($scope) {
		$scope.phones = [
			{"name": "Nexus S",
			"snippet": "Fast just got faster with Nexus S.",
			"age": 0},
			{"name": "Motorola XOOM™ with Wi-Fi",
			"snippet": "The Next, Next Generation tablet.",
			"age": 1},
			{"name": "MOTOROLA XOOM™",
			"snippet": "The Next, Next Generation tablet.",
			"age": 2}
		];
		$scope.orderProp = 'age';
	}
  </script>
</head>
<body ng-controller="PhoneListCtrl">
Search: <input ng-model="query">
Sort by:
<select ng-model="orderProp">
  <option value="name">Alphabetical</option>
  <option value="age">Newest</option>
</select>
<ul class="phones">
  <li ng-repeat="phone in phones | filter:query | orderBy:orderProp">
    {{phone.name}}
    <p>{{phone.snippet}}</p>
  </li>
</ul>
</body>
</html>
```

我们做了如下更改完成一个多了一个下拉菜单，它可以允许控制电话排列的顺序的功能。新增内容解释如下：

 - 首先，我们增加了一个叫做`orderProp`的`<select>`标签，这样我们的用户就可以选择我们提供的两种排序方法。
 - 然后，在`filter`过滤器后面添加一个`orderBy`过滤器用其来处理进入迭代器的数据。`orderBy`过滤器以一个数组作为输入，复制一份副本，然后把副本重排序再输出到迭代器。
 - AngularJS在`select`元素和`orderProp`模型之间创建了一个双向绑定。而后，`orderProp`会被用作`orderBy`过滤器的输入。
 - 对于控制器，对每个手机记录增加了age字段来作为排序判断的依据。而且加了一行让`orderProp`的默认值为`age`。如果我们不设置默认值，这个模型会在我们的用户在下拉菜单选择一个顺序之前一直处于未初始化状态。
 
现在我们该好好谈谈双向数据绑定了。注意到当应用在浏览器中加载时，`Newest`在下拉菜单中被选中。这是因为我们在控制器中把`orderProp`设置成了`age`。所以绑定在从我们模型到用户界面的方向上起作用——即数据从模型到视图的绑定。现在当你在下拉菜单中选择`Alphabetically`，数据模型会被同时更新，并且手机列表数组会被重新排序。这个时候数据绑定从另一个方向产生了作用——即数据从视图到模型的绑定。

##XHR和依赖注入












**工作之余会持续更新**
