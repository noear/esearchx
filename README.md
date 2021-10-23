# esearchx

基于 okhttp + snack3 开发，是一个代码直白且轻巧的 Elasticsearch ORM 框架

项目里有3个关键的对象概念：

* 执行上下文
* 查询器
* 命令

**主要逻辑：**

[查询器] 构建 [命令] 交由 [执行上下文] 执行

**演示：**

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>esearchx</artifactId>
    <version>1.0</version>
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
        
        //一个简单的查询
        LogDo result = esx.indice("user_log").selectById("1");

        //一个带条件的查询
        EsPage<LogDo> result = esx.indice("user_log")
                .where(r -> r.term("level", 5))
                .orderByDesc("log_id")
                .limit(50)
                .select(LogDo.class);


        //一个复杂些的查询
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().must()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .orderByAsc("level")
                .andByAsc("log_id")
                .minScore(1)
                .limit(50, 50)
                .select(LogDo.class);
    }
}

```

