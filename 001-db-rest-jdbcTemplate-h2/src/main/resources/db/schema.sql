

CREATE TABLE employee (
    id      INT             NOT NULL    AUTO_INCREMENT  ,
    birth_date  DATE            NOT NULL,
    first_name  VARCHAR(14)     NOT NULL,
    last_name   VARCHAR(16)     NOT NULL,
    gender      ENUM ('M','F')  NOT NULL,    
    hire_date   DATE            NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE t_student(
    no VARCHAR(14)  NOT NULL      ,
    name  VARCHAR(14)     NOT NULL,
    PRIMARY KEY (no)
);


CREATE TABLE department (
    id     CHAR(4)              NOT NULL,
    dept_name   VARCHAR(40)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE  KEY (dept_name)
);


CREATE TABLE `test` (
  `no` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `addr` varchar(100)  NULL,
  PRIMARY KEY (`no`,`name`)
);




CREATE TABLE complex_query_config (
  id int NOT NULL,
  method varchar(10)  NOT NULL,
  path varchar(100)   NOT NULL,
  status tinyint NOT NULL,
  sql text  NOT NULL,
  create_at datetime NOT NULL,
  update_at datetime NOT NULL
)  ;
