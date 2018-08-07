在进行检索前我们首先要有数据。

## LogStash日志搜集工具
LogStash几乎是与ES配套使用的一个玩意，它的官方推荐用法是从服务器中抓取日志发送到ES中，然后使用Kibana进行日志的监控，也就是著名的ELK架构。后来有人制作了jdbc-input的插件使LogStash可以进行数据库记录的抓取，逐渐流行后LogStash在后续版本升级中内置了这个插件，我们这次也是使用这个插件来操作数据库。

先看一下我们现在LogStash测试配置文件：

```
// 数据输入
input {
   // 使用数据库方式，除此之外还有file、beats端口等方式
  //第一个输入流，进行增量同步
  jdbc {
    //指定连接数据库驱动
    jdbc_driver_library => "/data/TRS/logstash-6.2.2/mysql/mysql-connector-java-5.1.46/mysql-connector-java-5.1.46-bin.jar"
   //驱动类名
    jdbc_driver_class => "com.mysql.jdbc.Driver"
   //数据库地址
    jdbc_connection_string => "jdbc:mysql://10.159.63.110:3306/discuz151231"
   //用户名
    jdbc_user => 
   //密码
    jdbc_password => 
   //执行时间表达式，此处1min一次
    schedule => "*/1 * * * *"	
   //时区
    jdbc_default_timezone => "Asia/Shanghai"
   //执行的sql文件路径
    statement_filepath => "/data/TRS/logstash-6.2.2/mysql/threadInsertSql.sql"
   //是否需要记录某个column的值
    use_column_value  => "true"
   //记录的column是tid字段
    tracking_column => "tid"
   //是否记录上次执行结果
    record_last_run => "true"
   //上次记录的结果持久化到txt文件
    last_run_metadata_path => "/data/TRS/logstash-6.2.2/mysql/last_run_insert.txt"
   //这个类型是用来分类output的
   type => "testcommunitythread"
  }
  //第二个输入流，进行数据更新
   jdbc {
    jdbc_driver_library => "/data/TRS/logstash-6.2.2/mysql/mysql-connector-java-5.1.46/mysql-connector-java-5.1.46-bin.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://10.159.63.110:3306/discuz151231"
    jdbc_user => 
    jdbc_password => 
   //10min一次
    schedule => "*/10 * * * *"	
    jdbc_default_timezone => "Asia/Shanghai"
    statement_filepath => "/data/TRS/logstash-6.2.2/mysql/threadUpdateSql.sql"
   //记录上次执行结果，不指定column的话默认track执行时间
    record_last_run => "true"
   //把这次执行时间持久化
    last_run_metadata_path => "/data/TRS/logstash-6.2.2/mysql/last_run_updateTime.txt"
   type => "testcommunitythread"
  }
}

//这里可以加filter进行数据清洗，暂不需要

//数据输出
output {
	//这就是上面提到的类型
	if [type] == "testcommunitythread" {
		//数据发送到ES
		elasticsearch {
			//ES服务器地址
			hosts => "10.159.37.219:9200"
			//ES的索引，相当于数据库的库名，必须全为小写
			index => "testcommunityindex"
			//用什么来作为主键，这里用tid
			document_id => "%{tid}"			
			//用json进行调试
			codec => "json"
		}	
	} 
	//如果还有别的type下面加else，else里面和上面的一样
}
```

sql文件中就写上你想执行的sql就行了。是不是很简单。

如果我不小心接手了这个God damn模块，我该怎么搞这东西？
好吧，毕竟你还有人可以问。
 - 去LogStash根目录下找到配置文件，改成你想要的配置，保存
 - 如果有sql和上次执行记录的变化，把那些文件也改了
 - 把LogStash进程杀掉，ps aux|grep LogStash  找到进程id， kill -9 id
 - 在LogStash根目录下，nohup bin/logstash -f mysql/logstash-mysql.conf & 后台挂起，如果你的路径和我的不一样，改一下路径就行了
 - 去logs文件夹下，看一下logStash-plain.log日志或者直接根目录下看nohup.out也行，有报错可能是配置文件引起的，sql报错也会有提示

好了，去ES看看你的数据库发过去了没吧。
社群数据同步逻辑：
 - 第一个input进行增量同步，logStash会记录上次记录到的id，下次执行时从这个id开始查询。
 - 第二个input进行数据更新，在帖子表中加入了一个触发器，当某些我们关注的字段进行更新时会在另一张表cloud_search_threadTrigger中记录tid和updateTime，这个input就会从这个表中取上次执行到现在这个时间段更新过的tid把它们的字段全查出来发往ES，对于ES来说更新就是插入，这样就完成了老数据的更新。
 - 访问发往ES的数据可以通过 http://10.159.37.226:9200/testcommunityindex/doc/_search?pretty 来查看

## ES全文搜索引擎
社群的全文检索引擎使用的是当前火热的ElasticSearch(以下简称ES)，它是一个使用Java开发的基于Lucene(Apache的一个开源全文搜索引擎工具包)的分布式检索服务器，设计用于云计算，安装十分简单，可以不断线进行服务器集群的扩展，稳定性很高，现在Gayhub、Wikipedia、StackOverflow等超级牛逼的公司已经把他们的搜索引擎换成了ES或者用了ES的组件，可见ES的强大。如果对ES内部运作原理感兴趣可以读一下官方文档，中文版，很不错。 https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html

ES的概念简单的说一下，一台服务器成为一个节点(node)，多个节点成为一个集群(cluster)，如果想要在线往集群中加node，那么
 - 买台服务器，装上ES，把配置文件的cluster.name写的和别的那些一样，ES就会自动咻！到这个node，把它并入集群了，原理见  https://www.elastic.co/guide/cn/elasticsearch/guide/current/important-configuration-changes.html#unicast
ES的主旨是随时可用和按需扩容，水平扩容和垂直扩容，说大白话就是多买服务器和买好服务器。
ES的配置文件基本上就是些，声明集群名，节点名，端口号，host，是否作为主从的东西，打开配置文件从命名上一看就明白，这就不说了。
打开浏览器可以看一下ES目前的
 - 集群状态 http://10.159.37.226:9200/ 集群健康 http://10.159.37.226:9200/_cluster/health?pretty 注意status字段是不是green
 - 节点状态 http://10.159.37.226:9200/_cat/nodes?v
 - 分片状态 http://10.159.37.226:9200/_cat/shards?v
 - index状态 http://10.159.37.226:9200/_cat/indices?pretty
 - 所有index的字段mapping http://10.159.37.226:9200/_mapping?pretty

打了一堆基本原理想想还是算了，我说的哪有官方说的好，想知道去翻书吧，我就用大白话说下易懂的，虽然不能帮助你了解运作方式，但是能迅速入门并可以进行一些基本的操作。index其实就相当于一台服务器的一个库，type就是一个表。 http://10.159.37.219:9200/_search?pretty 查看一下所有index的所有type的所有文档，恩，ES把数据库的一条记录称为一个文档。有时你可能看到分片的概念，其实是ES把一个index的数据分别放在不同的分片中，检索的时候从每个分片中取可以提升速度,这个对用户而言是透明的不懂也没事，如果你看过美剧硅谷会对这东西有更好的了解。
如果想要对ES进行索引文档的种种操作，可以直接在服务器用RESTful的方式进行操作，如curl -X PUT '10.159.37.219:9200/myIndex' 来创建一个index，同理DELETE删除等。

## ES提供的JavaApi
有了数据我们就可以在项目中进行搜索了！ES的检索比较特别，要求在GET请求中带有数据体，返回的格式都是JSON，你可以用curl在服务器里进行简单的检索。
ES提供了很全面的JavaApi，但是由于版本更新速度很快，你在网上找到的大部分都是一些老版本的Api，现在根本不能用，在老夫踩了一万个坑之后吐血整理了一份基于最新版本6.2的Api可以让你迅速进行问题的修复和扩展。
 
 - 在项目中添加如下Maven坐标：
	
```xml
<!--ES-->
<dependency>
	<groupId>org.elasticsearch.client</groupId>
	<artifactId>transport</artifactId>
	<version>6.2.2</version>
	<exclusions>
		<exclusion>
			<artifactId>httpclient</artifactId>
			<groupId>org.apache.httpcomponents</groupId>
		</exclusion>
	</exclusions>
</dependency>

<dependency>
	<groupId>org.elasticsearch</groupId>
	<artifactId>elasticsearch</artifactId>
	<version>6.2.2</version>
</dependency>

<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpclient</artifactId>
	<version>4.5.3</version>
</dependency>
```

最下面那个依赖和上面移除的httpcomponents的包是一个坑，如果你在项目中删除它们不会爆什么错就删掉吧。
如果ES相关类再调用时出现类似ClassNotFound、ClassDefNotFound之类的东西就是Maven依赖出现问题了，在调用ES的和ES所在的模块中都加入POM依赖没准就好了。注意JavaApi版本号要和服务器的ES版本一致。

 - ES使用TrasnportClient类进行Api调用，在SpringBoot启动类中加入此Bean的初始化，指定服务器ip，集群名，端口号。注意这里的端口号与从浏览器访问的端口号不同，默认9300，如果服务器这个端口开了防火墙是访问不到的。然后在你的类中就可以注入它开始使用了。

```java
@Bean
public TransportClient initTransportClient(){
	try{
		String ip = commonConstantsProperties.getEsServerIp();
		String clusterName = commonConstantsProperties.getEsClusterName();
		Integer port = commonConstantsProperties.getEsApiPort();
		//配置文件，设置集群名称
		Settings settings = Settings.builder().put("cluster.name",clusterName).build();
		//连接ES开启的JavaApi接口
		return new PreBuiltTransportClient(settings)
			.addTransportAddress(newTransportAddress(InetAddress.getByName(ip),port));
	} catch(Exceptione){
		e.printStackTrace();
	}
	return null;
}
```

 - 设置检索条件在程序中就是在组装SearchRequestBuilder对象
	
```java
client
//查询哪个index哪个type
.prepareSearch(index).setTypes(type)
//查询方式，两种，快而粗，慢而细
.setSearchType(searchType)
//matchQuery全文搜索，指定检索字段和搜索关键词，termQuery是精确搜索
.setQuery(QueryBuilders.matchQuery(field,searchKey))
//过滤器，如指定某字段必须为什么
.setPostFilter(QueryBuilders.termQuery(field,searchKey))
//分页
.setFrom(pageNo).setSize(pageSize).setExplain(true)
//Get请求ES接口
.get()
```

上面写了一个简单的搜索代码，setQuery()中的参数可以传大量的Builder，这里不全说，因为太多了，上面介绍了两种，注释里也介绍他们是做什么的，还有两种我们常用的Builder

```java
DisMaxQueryBuilder maxQueryBuilder = QueryBuilders.disMaxQuery();
for(Stringfield : searchField)
	maxQueryBuilder.add(QueryBuilders.matchQuery(field,searchKey));
requestBuilder.setQuery(maxQueryBuilder);
```

这个适用于进行多于一个字段的全文检索，综合评分，如帖子表检索时要检索标题内容和标签。

```java
BoolQueryBuilderboolQueryBuilder=QueryBuilders.boolQuery();
boolQueryBuilder.must(QueryBuilders.termQuery(field,searchKey));
boolQueryBuilder.should(QueryBuilders.termQuery(field,searchKey));
boolQueryBuilder.mustNot(QueryBuilders.termQuery(field,searchKey));
requestBuilder.setQuery(boolQueryBuilder);
```

这个适用于复杂的检索，如某几个字段必须为什么must，某个字段可以为好多值should，某个字段不能为什么mustNot，可以使用bool查询。
SearchRequestBuilder中还可以设置某些字段进行关键字高亮，这个在程序里自己做也可以。

```java
String[]highLightField=sb.getHighLightField();
//组装高亮builder
HighlightBuilder highlightBuilder = new HighlightBuilder();
for(Stringfield:highLightField)
highlightBuilder.field(field);
highlightBuilder.preTags(sb.getHighLightPreTag());
highlightBuilder.postTags(sb.getHighLightPostTag());
highlightBuilder.fragmentSize(30000);
//加入请求builder
requestBuilder.highlighter(highlightBuilder);
```

可以来设置高亮的前后标签是什么，指定哪些字段。加粗的地方也是个坑，如果不加的话返回高亮字段替换原字段的时候会有截断，自己设置你的高亮字段最长是多少。
 - 现在我们疯狂操作一波拿到了一堆数据可以来进行处理了。下面这是我的demo中的一部分，还包含了对高亮字段的处理：
	
```java
//调用ES接口进行搜索
SearchResponse response = esService.getFullTextResponse(builder);
//命中文档处理
SearchHits hits = response.getHits();
for(SearchHithit:hits){
	//将命中对象转化为map
	Map<String,Object> resultMap = hit.getSourceAsMap();
	//高亮域map
	Map<String,HighlightField> highlightFieldMap = hit.getHighlightFields();
	//如果设置了高亮
	if("true".equals(isHighLight)){
		for(Stringfield : highLightFields.split(",")){
			if(highlightFieldMap!=null && highlightFieldMap.size()>0){
				//高亮字段
				HighlightField highlightField = highlightFieldMap.get(field);
					if(highlightField!=null){
						//用高亮的字符串进行原文本替换
						Text[] texts = highlightField.getFragments();
						if(texts!=null && texts.length>0)
							resultMap.put(field,texts[0].string());
					}
			}
		}
	}
	//加入返回列表
	resultList.add(resultMap);
}
```

基本用法demo中都已经涉及了。返回的list就是我们熟悉的数据结构了。
偶对返回的list中也有坑，Date类型的数据ES中存的是XMLSchema的类型，用SimpleDateFormat把format设置成" yyyy-MM-dd'T'HH:mm:ss.SSS'Z' "就行了。
																
						All Rights Reserved By Author ： JanusMix
