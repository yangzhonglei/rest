# db-rest-jdbcTemplate-h2

把数据库里的所有表统一暴露为rest的crud 接口


---

spring boot  2.3.0
spring JdbcTemplate
数据库连接池  hikari -- springboot 2.0 中默认连接池是Hikari
数据库 h2
jdk8


把数据库里的所有表  都暴露为rest服务   
这里是使用 JdbcTemplate实现的
当前实现的存在的问题：

1、会有sql注入的问题
2、对于访问哪些表没有控制
3、直接使用的表名 和 列名 ， 没有映射为Java的类，没有使用驼峰命名
4、主键不是 id 的问题
5、感觉自己这个实现的有点low
6、下一步研究下  人家的实现 https://github.com/downgoon/autorest4db

---

## QuickStart

运行类  ： com.yzl.study.db2rest.Application


---
## CRUD operations



- **list entity**

``` bash
curl -i -X GET http://172.18.49.66:8080/rest/employee
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:10:37 GMT

[
    {
        "ID": 10001,
        "BIRTH_DATE": "1953-09-02",
        "FIRST_NAME": "Georgi",
        "LAST_NAME": "Facello",
        "GENDER": "M",
        "HIRE_DATE": "1986-06-26"
    },
    {
        "ID": 10002,
        "BIRTH_DATE": "1964-06-02",
        "FIRST_NAME": "Bezalel",
        "LAST_NAME": "Simmel",
        "GENDER": "F",
        "HIRE_DATE": "1985-11-21"
    }
]

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
curl -i -X GET http://172.18.49.66:8080/rest/employee?FIRST_NAME=Georgi
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:13:39 GMT

[{"ID":10001,"BIRTH_DATE":"1953-09-02","FIRST_NAME":"Georgi","LAST_NAME":"Facello","GENDER":"M","HIRE_DATE":"1986-06-26"}]
``` 



- **create entity**

``` bash
$ curl -X POST   -i  -d  '{"ID":10005,"BIRTH_DATE":"1953-09-02","FIRST_NAME":"zhonglei","LAST_NAME":"yang","GENDER":"M","HIRE_DATE":"1986-06-26"}'  -H 'Content-Type: application/json'   http://172.18.49.66:8080/rest/employee

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:28:17 GMT

``` 
id 设置为自动生成的话  可以不用指定 id的值



- **update entity**

``` bash
$ curl -X PUT   -i  -d  '{"FIRST_NAME":"aaaaaaa"}'  -H 'Content-Type: application/json'   http://172.18.49.66:8080/rest/employee/10001
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:35:39 GMT

``` 




- **delete entity**
``` bash
$ curl -X DELETE   -i  http://172.18.49.66:8080/rest/employee/10001
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 May 2020 02:38:11 GMT
```






