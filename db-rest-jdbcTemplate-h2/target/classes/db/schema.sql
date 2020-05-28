

CREATE TABLE employee (
    id      INT             NOT NULL    AUTO_INCREMENT  ,
    birth_date  DATE            NOT NULL,
    first_name  VARCHAR(14)     NOT NULL,
    last_name   VARCHAR(16)     NOT NULL,
    gender      ENUM ('M','F')  NOT NULL,    
    hire_date   DATE            NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE department (
    id     CHAR(4)              NOT NULL,
    dept_name   VARCHAR(40)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE  KEY (dept_name)
);


