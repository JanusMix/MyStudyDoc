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

可以通过ls获取当前目录的所有文件信息，然后通过Sort -Descending对文件信息按照Name降序排列，最后将排序好的文件的Name和Mode格式化成Table输出。

###重定向
可以将命令的输出保存到文件中，'>'为覆盖,'>>'为追加。

###数学运算
可以直接在命令行输入数学公式当作计算器使用，除了基本运算符还可以识别KB TB等计算机存储符号。

###执行外部命令
Ps可以像cmd一样执行外部命令，如使用`netstat`查看网络端口状态，`ipconfig`查看网络配置，`route print`查看路由信息，就跟在win+R输入得到的东西一样。

###Cmdlets
Cmdlets是Ps的内部命令，由一个动词一个名词组成，数量相当多，需要用时现查即可。

###Alias
那么多的Cmdlets记不住怎么办，而且记住了敲那么长也很麻烦，别名可以大致解决这个问题。Ps内置了许多别名，大多都来源于bash和cmd，如ls和dir，对应cmdlet的Get-ChildItem。
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
将经常使用的参数count 2固化到别名中，$args为参数的占位符，使用时要加上参数。

###执行文件和脚本
Ps中直接输入文件名无法执行此文件，需要前面加./，但是在执行本地脚本时会默认禁用执行脚本。可以以如下方式解决：

 - 以管理员身份运行Ps
 - 输入set-executionpolicy
 - 输入remotesigned
 - 输入y
 
 
set-executionpolicy有四种值：
 - Restricted   默认，不允许任何脚本运行
 - AllSigned    只能运行经过数字签名的脚本
 - RemoteSigned 运行本地的脚本不需要数字签名，但是运行从网络上下载的脚本就必须要有数字签名
 - Unrestricted 允许所有脚本运行

这样就可以执行脚本了。



