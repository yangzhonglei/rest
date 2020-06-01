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
碎碎念：
这里是使用 JdbcTemplate实现的
当前实现的存在的问题：
1. 会有sql注入的问题--修改了
2. 对于访问哪些表没有控制--增加了限制 通过配置文件限制
3. 直接使用的表名 和 列名 ， 没有映射为Java的类，没有使用驼峰命名--已更改为使用java风格的对象名
4. 主键不是 id 的问题--增加了判断
5. 感觉自己这个实现的有点low--是的
6. 下一步研究下  人家的实现 https://github.com/downgoon/autorest4db
7. 通过可配置排除掉某些字段
8. 

---

## QuickStart

运行类  ： com.yzl.study.db2rest.Application


---
## CRUD operations



- **list entity**

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






- **list entity 分页 排序**

``` bash
curl -i -X GET  http://172.18.49.66:8080/rest/employee?page=1&size=2&sort=id,desc&sort=gender
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




- **view entity**

``` bash
curl -i -X GET http://172.18.49.66:8080/rest/employee/10001
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:12:28 GMT

{"ID":10001,"BIRTH_DATE":"1953-09-02","FIRST_NAME":"Georgi","LAST_NAME":"Facello","GENDER":"M","HIRE_DATE":"1986-06-26"}
```   


- **filter entity**

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



- **create entity**

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



- **update entity**

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




- **delete entity**
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









