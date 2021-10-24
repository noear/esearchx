[![Maven Central](https://img.shields.io/maven-central/v/org.noear/esearchx.svg)](https://mvnrepository.com/search?q=esearchx)

` QQ交流群：22200020 `


# EsearchX for java

基于 okhttp + snack3 开发，是一个代码直白和简单的 Elasticsearch ORM 框架

项目里有3个关键的对象概念：

* 执行上下文
* 查询器
* 命令

支持自动序列化和反序列化，以及批量插入、批量更新

### 主要逻辑：

[查询器] 构建 [命令] 交由 [执行上下文] 执行

### 演示：

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>esearchx</artifactId>
    <version>1.0.2</version>
</dependency>
```

```java
//
// 更多的示例，可以查看 src/test/ 下的相关单测
//
public class DemoApp {
    String tableCreateDsl = "...";

    public void demo() {
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
        
        //批量插入
        List<LogDo> list = new ArrayList<>();
        list.add(new LogDo());
        esx.indice("user_log").insertList(list);
        
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
    }
}

```

