# esearchx

基于 okhttp + snack3 开发，是一个白代码直接又很轻的 Elasticsearch ORM 框架

主要涉及3个概念：

* 查询上下文
* 查询器
* 命令


**演示：**

```java
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

        //删除索引（如果存在就删了）
        if (esx.indiceExist("user_log_20200101")) {
            esx.indiceDrop("user_log_20200101");
        }


        //一个简单查询
        EsPage<LogDo> result = esx.indice("user_log")
                .where(r -> r.term("level", 5))
                .limit(50)
                .select(LogDo.class);


        //一个复杂点的查询
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

