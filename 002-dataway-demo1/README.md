

dataway  参考 https://www.hasor.net/    


配置一个查询 ：  
```
// 声明一个 dataQL
var dataSet = @@sql(id,id2) <%
    select * from department where id = #{id} or id = #{id2} ;
%>
// 执行这个 SQL，并返回结果
return dataSet(${id},${id2});

```



一个分页查询   
```
// SQL 执行器切换为分页模式
hint FRAGMENT_SQL_QUERY_BY_PAGE = true
// 定义查询SQL
var dataSet = @@sql() <%
    select * from employee 
%>
// 创建分页查询对象
var pageQuery =  dataSet();
// 设置分页信息
run pageQuery.setPageInfo({
    "pageSize"    : 3, // 页大小
    "currentPage" : 1 // 第3页
});
// 执行分页查询
var result = pageQuery.data();

return result
```



 
当前看使用 dataway  可以快速开发 复杂一些的sql查询   暴露为http接口   
想快速开发一些项目的时候可以考虑使用      
 





