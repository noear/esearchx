[![Maven Central](https://img.shields.io/maven-central/v/org.noear/esearchx.svg)](https://mvnrepository.com/search?q=esearchx)
[![Apache 2.0](https://img.shields.io/:license-Apache2-blue.svg)](https://license.coscl.org.cn/Apache2/)
[![JDK-8+](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
[![QQ交流群](https://img.shields.io/badge/QQ交流群-22200020-orange)](https://jq.qq.com/?_wv=1027&k=kjB5JNiC)


# EsearchX for java

基于 okhttp + snack3 开发，是一个代码直白和简单的 Elasticsearch ORM 框架。支持 7.x , 8.x

项目里有3个关键的对象概念：

* 执行上下文
* 查询器
* 命令

支持自动序列化和反序列化，以及批量插入、批量更新；脚本查询、聚合查询。

### 快速入门：

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>esearchx</artifactId>
    <version>1.0.15-M1</version>
</dependency>
```

```java
//
// 更多的示例，可以查看 src/test/ 下的相关单测
//
public class DemoApp {
    String tableCreateDsl = "...";

    public void demo() {
        //执行前打印dsl
        EsGlobal.onCommandBefore(cmd -> System.out.println("dsl:::" + cmd.getDsl()));
        
        //实例化上下文
        EsContext esx = new EsContext("localhost:30480");
        

        //创建索引
        esx.indiceCreate("user_log_20200101", tableCreateDsl);
        esx.indiceCreate("user_log_20200102", tableCreateDsl);
        esx.indiceCreate("user_log_20200103", tableCreateDsl);

        //构建索引别名
        esx.indiceAliases(a -> a
                .add("user_log_20200101", "user_log")
                .add("user_log_20200102", "user_log")
                .add("user_log_20200103", "user_log"));
        
        //删除索引（如果存在就删了；当然也可以直接删）
        if (esx.indiceExist("user_log_20200101")) {
            esx.indiceDrop("user_log_20200101");
        }

        //单条插入
        LogDo logDo = new LogDo();
        esx.indice("user_log").insert(logDo);
        
        //批量插入
        List<LogDo> list = new ArrayList<>();
        list.add(new LogDo());
        esx.indice("user_log").insertList(list);

        //单条插入或更新
        LogDo logDo = new LogDo();
        esx.indice("user_log").upsert("1", logDo);
        
        //批量插入或更新
        Map<String, LogDo> list = new LinkedHashMap<>();
        list.put("...",new LogDo());
        esx.indice("user_log").upsertList(list);
        
        //一个简单的查询
        LogDo result = esx.indice("user_log").selectById("1");
        
        //一个带条件的查询
        EsData<LogDo> result = esx.indice("user_log")
                .where(r -> r.term("level", 5))
                .orderByDesc("log_id")
                .limit(50)
                .selectList(LogDo.class);

        //一个结果高亮的查询
        Map result = context.indice(indice)
                .where(c -> c.match("content", "tag"))
                .highlight(h -> h.addField("content", f -> f.preTags("<em>").postTags("</em>")))
                .limit(1)
                .selectMap();
        
        //一个复杂些的查询
        EsData<LogDo> result = esx.indice(indice)
                .where(c -> c.useScore().must()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .orderByAsc("level")
                .andByAsc("log_id")
                .minScore(1)
                .limit(50, 50)
                .selectList(LogDo.class);

        //脚本查询
        EsData<LogDo> result = esx.indice(indice)
                .where(c -> c.script("doc['tag'].value.length() >= params.len", p->p.set("len",2)))
                .limit(10)
                .selectList(LogDo.class);
        
        //聚合查询
        ONode result = esx.indice(indice)
                .where(w->w.term("tag","list1"))
                .limit(0)
                .aggs(a -> a.terms("level", t -> t.size(20))
                            .aggs(a1 -> a1.topHits(2, s -> s.addByAes("log_fulltime"))))
                .selectAggs();
    }
}

```

