# db-rest-jdbcTemplate-h2

把数据库里的用户表统一暴露为rest的CRUD 接口。  
使用spring jdbcTemplate实现--简单粗暴。  
对于springboot的后台api程序 使用本程序模块，直接实现对数据库单表的 restfull CRUD操作。  
当前只支持mysql数据库，oracle的分页有些不同，稍后修改兼容。  

---
## 技术框架    
spring boot  2.3.0  
spring JdbcTemplate  
数据库连接池  hikari -- springboot 2.0 中默认连接池是Hikari  
测试使用h2数据库  
jdk8  

---
问题：  
这里是使用 JdbcTemplate实现的   
当前实现的存在的问题： 
1. 会有sql注入的问题--修改了
2. 对于访问哪些表没有控制--增加了限制 通过配置文件限制
3. 直接使用的表名 和 列名 ， 没有映射为Java的类，没有使用驼峰命名--已更改为使用java风格的对象名
4. 主键不是 id 的问题--增加了判断
5. 感觉自己这个实现的有点low--是的
6. 下一步研究下  人家的实现 https://github.com/downgoon/autorest4db
7. 通过可配置排除掉某些字段
8. 数据库动态增加表的问题：要考虑动态刷新记录的表信息

---

## QuickStart

运行类  ： com.yzl.study.db2rest.Application


---
## CRUD operations



- **1. list entity**

``` bash
curl -i -X GET http://172.18.49.66:8080/rest/employee
HTTP/1.1 200 
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Date: Mon, 01 Jun 2020 09:06:33 GMT

{
    "data": {
        "page": 1,
        "size": 10,
        "total": 2,
        "list": [
            {
                "hireDate": "1986-06-26",
                "gender": "M",
                "id": 10001,
                "birthDate": "1953-09-02"
            },
            {
                "hireDate": "1985-11-21",
                "gender": "F",
                "id": 10002,
                "birthDate": "1964-06-02"
            }
        ]
    },
    "msg": "成功",
    "status": "SUCCESS",
    "timeStamp": 1591002344810
}
```   






- **2. list entity 分页 排序**

``` bash
curl -i -X GET  http://172.18.49.66:8080/rest/employee?page=1\&size=2\&sort=id,desc\&sort=gender
HTTP/1.1 200 
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Date: Mon, 01 Jun 2020 09:10:50 GMT

{
    "data": {
        "page": 1,
        "size": 2,
        "total": 2,
        "list": [
            {
                "hireDate": "1985-11-21",
                "gender": "F",
                "id": 10002,
                "birthDate": "1964-06-02"
            },
            {
                "hireDate": "1986-06-26",
                "gender": "M",
                "id": 10001,
                "birthDate": "1953-09-02"
            }
        ]
    },
    "msg": "成功",
    "status": "SUCCESS",
    "timeStamp": 1591002804183
}
```   

<font color=red >
使用mysql的情况下可以指定使用汉语言评语排序：使用gbkconvert$前缀  
 http://172.18.49.66:8080/rest/employee?page=1\&size=20\&sort=gbkconvert$firstName
</font>
 
 



- **3. view entity**

``` bash
curl -i -X GET http://172.18.49.66:8080/rest/employee/10001
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:12:28 GMT

{"ID":10001,"BIRTH_DATE":"1953-09-02","FIRST_NAME":"Georgi","LAST_NAME":"Facello","GENDER":"M","HIRE_DATE":"1986-06-26"}
```   


- **4. filter entity**

4.1  普通字段过滤  
``` bash
curl -i -X GET http://172.18.49.66:8080/rest/employee?firstName=Georgi
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:13:39 GMT

{
	"data": {
		"page": 1,
		"size": 10,
		"total": 1,
		"list": [{
				"hireDate": "1986-06-26",
				"gender": "M",
				"id": 10001,
				"birthDate": "1953-09-02"
			}
		]
	},
	"msg": "成功",
	"status": "SUCCESS",
	"timeStamp": 1591003067979
}
``` 
4.2  扩展字段过滤：  使用 isNull、isNotNull 过滤记录  

4.2.1  isNotNull:  &前的"\\"  是为了在linux下转义  
```
curl -i -X  GET http://172.18.49.66:8080/rest/test?page=1\&size=20\&addr=isNotNull

HTTP/1.1 200
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Date: Mon, 13 Jul 2020 03:48:11 GMT

{
    "data":{
        "page":1,
        "size":10,
        "total":1,
        "list":[
            {
                "no":"b",
                "name":"Finance",
                "addr":"beijing"
            }]
    },
    "msg":"成功",
    "status":"SUCCESS",
    "timeStamp":1594612091213
}
```

4.2.1  isNull:  &前的"\\"  是为了在linux下转义  
```
http://172.18.49.66:8080/rest/test?page=1\&size=20\&addr=isNull

HTTP/1.1 200
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Date: Mon, 13 Jul 2020 03:50:29 GMT

{
    "data":{
        "page":1,
        "size":10,
        "total":1,
        "list":[
            {
                "no":"a",
                "name":"Development",
                "addr":null
            }]
    },
    "msg":"成功",
    "status":"SUCCESS",
    "timeStamp":1594612229017
}

```





- **5. create entity**

``` bash
$ curl -X POST   -i  -d  '{ "birthDate":"1953-09-02","firstName":"zhonglei","lastName":"yang","gender":"M","hireDate":"1986-06-26"}'  -H 'Content-Type: application/json'   http://172.18.49.66:8080/rest/employee

HTTP/1.1 200 
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Date: Mon, 01 Jun 2020 09:17:06 GMT

{
	"msg": "成功",
	"status": "SUCCESS",
	"timeStamp": 1591003026216
}

``` 
id 设置为自动生成的话  可以不用指定 id的值



- **6. update entity**

``` bash
$ curl -X PUT   -i  -d  '{"firstName":"aaaaaaa"}'  -H 'Content-Type: application/json'   http://172.18.49.66:8080/rest/employee/10001
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:35:39 GMT

{
	"msg": "成功",
	"status": "SUCCESS",
	"timeStamp": 1591003158466
}

``` 




- **7. delete entity**
``` bash
$ curl -X DELETE   -i  http://172.18.49.66:8080/rest/employee/10001
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:38:11 GMT

{
	"msg": "成功",
	"status": "SUCCESS",
	"timeStamp": 1591003190181
}
```



------ 
## 配置说明  

配置employee的 listEntity可以访问:   
simple-rest-config.permit.employee-listEntity=true  

配置department的所有的方法都可以访问：  
simple-rest-config.permit.department-all=true  

配置employee查询结果 排除 firstName,lastName 字段:  
simple-rest-config.exclude.employee=firstName,lastName  



覆盖url问题:假如使用的simpleRest 访问 /rest/employee/10003并不能满足我们需求     
我们可以对这个url 做出定制调整，根据spring url的匹配规则 会优先匹配确定的url        
可以参考  TestController     



## 配置sql暴露为接口  

按照dataway的思路 在数据库增加表complex_query_config    
复杂的查询sql可以配置在这里   
通过  ComplexQueryController 暴露sql为 接口     
当前要求 方法必须是 POST application/json   

```
$ curl -i -X POST -H 'Content-Type: application/json' -d  '{"id":"0", "page": 1,  "size": 1 ,"sort":"lastName asc ,firstName desc" }' http://172.18.49.66:8080/query/employee3
HTTP/1.1 200 
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Date: Wed, 03 Jun 2020 09:26:33 GMT

{
	"data": {
		"page": 1,
		"size": 1,
		"total": 13,
		"list": [{
				"firstName": "Alejandro",
				"lastName": "Brender",
				"hireDate": "1988-01-19",
				"gender": "M",
				"id": 10039,
				"birthDate": "1959-10-01"
			}
		]
	},
	"msg": "成功",
	"status": "SUCCESS",
	"timeStamp": 1591176393156
}
```

