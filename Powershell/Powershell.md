#Powershell
> Powershell(以下简称Ps)是运行在windows机器上实现系统和应用程序管理自动化的命令行脚本环境。微软之所以把它前面加上power不是没有原因的，是因为它真的十分强大。
> 不单单是继承了Linux的bash和Windows的cmd，而且支持了.NET对象，易用性和可读性位于所有Shell之首不足为过。虽然Python远比Ps强大，不过总有些东西是Python
> 无法替代的，学点总是没有坏处。

让我们从头开始，把它当作一门新语言来入门试试。

参考[http://www.pstips.net/powershell-online-tutorials/](http://www.pstips.net/powershell-online-tutorials/) 网站进行学习，挑选比较重要易忘知识点如下罗列。

###管道
使用 | 可以把上一条命令的输出作为下一条命令的输入，如：

```bash
PS D:\>ls | sort -Descending Name | Format-Table Name,Mode
```

可以通过`ls`获取当前目录的所有文件信息，然后通过`Sort -Descending`对文件信息按照Name降序排列，最后将排序好的文件的Name和Mode格式化成Table输出。

###重定向
可以将命令的输出保存到文件中，'>'为覆盖,'>>'为追加。

###数学运算
可以直接在命令行输入数学公式当作计算器使用，除了基本运算符还可以识别KB TB等计算机存储符号。

###执行外部命令
Ps可以像cmd一样执行外部命令，如使用`netstat`查看网络端口状态，`ipconfig`查看网络配置，`route print`查看路由信息，就跟在win+R输入得到的东西一样。

###Cmdlets
Cmdlets是Ps的内部命令，由一个动词一个名词组成，数量相当多，需要用时现查即可。

###Alias
那么多的Cmdlets记不住怎么办，而且记住了敲那么长也很麻烦，别名可以大致解决这个问题。Ps内置了许多别名，大多都来源于bash和cmd，如ls和dir，对应cmdlet的`Get-ChildItem`。
###查询别名真实命令

```bash
PS D:\>get-alias -name ls
CommandType     Name                                                Definition
-----------     ----                                                ----------
Alias           ls                                                  Get-ChildItem
```
对了，Ps对大小写不敏感，大小写取决于个人习惯即可。

###查询所有可用别名

```bash
PS D:\>dir alias:
```

###创建自己的别名
如给notepad创建一个别名edit

```bash
PS D:\>set-alias -name edit -value notepad
```

别名不用删除，退出时自动清空，本来自带的不会清空。如果不想让自己的清空。可以导出别名到文件然后需要时导入。-force可以强制覆盖已有别名

```bash
PS D:\>export-alias alias.ps1
PS D:\>import-alias -force alias.ps1
```

通过这种方式创建的别名无法带参数，如果想要创建带参数的别名需要使用.NET的函数

```bash
PS D:\> function test-conn {test-connection -count 2 -computername $args}
PS D:\> set-alias tc test-conn
PS D:\> tc localhost
```
将经常使用的参数count 2固化到别名中，`$args`为参数的占位符，使用时要加上参数。

###执行文件和脚本
Ps中直接输入文件名无法执行此文件，需要前面加./，但是在执行本地脚本时会默认禁用执行脚本。可以以如下方式解决：

以管理员身份运行Ps

```bash
PS D:\> set-executionpolicy

位于命令管道位置 1 的 cmdlet Set-ExecutionPolicy
请为以下参数提供值:
ExecutionPolicy: remotesigned

执行策略更改
执行策略可帮助你防止执行不信任的脚本。更改执行策略可能会产生安全风险，如
http://go.microsoft.com/fwlink/?LinkID=135170 中的 about_Execution_Policies 帮助主题所述。是否要更改执行策略?
[Y] 是(Y)  [A] 全是(A)  [N] 否(N)  [L] 全否(L)  [S] 暂停(S)  [?] 帮助 (默认值为“N”): y
```
 
set-executionpolicy有四种值：

 - Restricted   默认，不允许任何脚本运行
 - AllSigned    只能运行经过数字签名的脚本
 - RemoteSigned 运行本地的脚本不需要数字签名，但是运行从网络上下载的脚本就必须要有数字签名
 - Unrestricted 允许所有脚本运行

这样就可以执行脚本了。

###变量

和Python一样，变量使用不需要声明类型，可以自动创建，前面以美元符$开头，如果变量名中有特殊字符，可以用{}括起来。因为Ps是支持对象的，所以可以使用任何类型赋值给变量。支持链式赋值和数值交换。

```bash
PS D:\> ${'a'=$sb} = "somebody"
PS D:\> ${'a'=$sb}
somebody
```

使用中的变量存储在`variable：`虚拟驱动中，有它的存在我们可以像查找文件那样使用通配符查找变量。如查找以value开头的变量：

```bash
PS D:\> ls variable:value*
```

验证是否有这个变量:

```bash
PS D:\> Test-Path variable:value1
true
```

变量可以在退出Ps时自动删除，非要手动删除的话可以像文件那样删除它。

####变量写保护
在使用new-variable创建变量时，可以使用`option`选项给变量加上只读属性，这样就不能给变量重新赋值了。

```bash
PS D:\> new-variable num -value 100 -force -option readonly
```

但是这种方式可以通过删除变量再创建的方式重新赋值，所以并不是最安全的。有一种权限更高的变量是constant，声明后不可修改。

```bash
PS D:\> new-variable num -value "strong" -option constant
```

可以在`new-variable`的时候加上`-description`添加变量描述，这个默认是不显示的，可以通过formal-list查看。

```bash
PS D:\> new-variable name -value "me" -description "This is my name"
```

####自动化变量
所谓自动化变量就是打开Ps自动加载的变量，这些变量存放的内容一般是：

 - 用户信息：如用户的根目录`$home`
 - 配置信息：如Ps控制台的一些基本设置
 - 运行时信息：如一个函数由谁调用，一个脚本的运行的目录等
 
其中大部分都只能读，不能改。下面是几个比较有意思的自动化变量:

 - $$ 包含会话所收到的最后一行中的最后一个令牌
 - $? 包含最后一个操作的执行状态。如果成功则为TRUE
 - $^ 包含会话所收到的最后一行中的第一个令牌
 - $_ 包含管道对象中的当前对象
 - $args 包含由未声明参数和/或传递给函数、脚本或脚本块的参数值组成的数组。在创建函数时可以声明参数，方法是使用param关键字或在函数名称后添加以圆括号括起、逗号分隔的参数列表。
 
等等还有很多。了解到这里之后我们可以把上面查询所有可用的别名的例子修改一下：

```bash
PS D:\> dir alias: | where {$_.definition.startswith("remove")}
```

就可以查看所有`defination`以remove开头的别名了，这里的`$_`指的就是循环所有别名的每一个。循环的事情我们以后再说。

 











