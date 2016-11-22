#Powershell
> Powershell(以下简称Ps)是运行在windows机器上实现系统和应用程序管理自动化的命令行脚本环境。微软之所以把它前面加上power不是没有原因的，是因为它真的十分强大。
> 不单单是继承了Linux的bash和Windows的cmd，而且支持了.NET对象，易用性和可读性位于所有Shell之首不足为过。、、

让我们从头开始，把它当作一门新语言来入门试试。

参考[http://www.pstips.net/powershell-online-tutorials/](http://www.pstips.net/powershell-online-tutorials/)网站进行学习，挑选比较重要易忘
的知识点如下罗列。

###管道
使用 | 可以把上一条命令的输出作为下一条命令的输入，如：

```bash
ls | sort -Descending Name | Format-Table Name,Mode
```

可以通过ls获取当前目录的所有文件信息，然后通过Sort -Descending对文件信息按照Name降序排列，最后将排序好的文件的Name和Mode格式化成Table输出。

###重定向
可以将命令的输出保存到文件中，'>'为覆盖,'>>'为追加



