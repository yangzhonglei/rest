

INSERT INTO `department` (`id`, `dept_name`) VALUES ('d009', 'Customer Service');
INSERT INTO `department` (`id`, `dept_name`) VALUES ('d005', 'Development');
INSERT INTO `department` (`id`, `dept_name`) VALUES ('d002', 'Finance');


INSERT INTO `t_student` (`no`, `name`) VALUES ('a', 'Development');
INSERT INTO `t_student` (`no`, `name`)  VALUES ('b', 'Finance');

INSERT INTO `test` (`no`, `name`) VALUES ('a', 'Development');
INSERT INTO `test` (`no`, `name`)  VALUES ('b', 'Finance');

INSERT INTO `employee` (`id`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) VALUES ('10001', '1953-09-02', 'Georgi', 'Facello', 'M', '1986-06-26');
INSERT INTO `employee` (`id`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) VALUES ('10002', '1964-06-02', 'Bezalel', 'Simmel', 'F', '1985-11-21');


INSERT INTO employee (id,birth_date,first_name,last_name,gender,hire_date) VALUES 
(10044,'1961-09-21','Mingsen','Casley','F','1994-05-21')
,(10035,'1953-02-08','Alain','Chappelet','M','1988-09-05')
,(10036,'1959-08-10','Adamantios','Portugali','M','1992-01-03')
,(10037,'1963-07-22','Pradeep','Makrucki','M','1990-12-05')
,(10038,'1960-07-20','Huan','Lortz','M','1989-09-20')
,(10039,'1959-10-01','Alejandro','Brender','M','1988-01-19')
,(10040,'1959-09-13','Weiyi','Meriste','F','1993-02-14')
,(10041,'1959-08-27','Uri','Lenart','F','1989-11-12')
,(10042,'1956-02-26','Magy','Stamatiou','F','1993-03-21')
,(10043,'1960-09-19','Yishay','Tzvieli','M','1990-10-20')
;

commit;


