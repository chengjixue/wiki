# wiki项目搭建问题总结

<h2>热部署3步骤</h2>

```java
1.添加 spring-boot-devtols(在pom.xml文件中引入)
2.打开顶部工具栏 File -> Settings -> Default Settings -> Build -> Compiler 然后勾选 Build project automatically 。
3.Ctrl + Shift + Alt + / 然后进入Registry ，勾选自动编译并调整延时参数。compiler.automake.allow.when.app.running -> 自动编译
4.compile.document.save.trigger.delay -> 自动更新文件
```

## Long类型精度丢失问题

在传值到前端的过程中，因为id为雪花算法生成的id传到前端的时候long类型的精度出现丢失问题，导致页面刷新后想删除改数据删除不了（原因：ID对不上）

解决办法

```java
//在resp包中在类型为Long类型的全部加上标明序列化方式（代码量不大采用）
@JsonSerialize(using = ToStringSerializer.class)
```

## CommonResp类的作用

在返回前端数据的时候，当多个对象都要返回时造成返回值混乱，

所以创建CommonResp<T>类的

```java
public class CommonResp<T> {
    /*
     * 业务上的登录成功或失败
     * */
    private boolean success = true;
    //    返回信息
    private String message;
    //    返回泛型数据，自定义数据类型
    private T content;
```








