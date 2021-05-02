CREATE TABLE farmer (
 id INT(11) NOT NULL AUTO_INCREMENT,
 first_name varchar(20) NOT NULL,
 middle_name varchar(20) NOT NULL,
 last_name varchar(20) NOT NULL,
 state_of_origin varchar(25) NOT NULL,
 local_government varchar(25) NOT NULL,
 ward varchar(20) NOT NULL,
 home_address varchar(65) NOT NULL,
 phonenum varchar(15) NOT NULL,
 bvn varchar(20) NOT NULL,
 nim varchar(20) NOT NULL, 
 groupid int NOT NULL,
 CONSTRAINT fk_fm_group FOREIGN KEY(groupid) REFERENCES fmgroup(id),
 PRIMARY KEY (id)
);

ALTER TABLE farmer AUTO_INCREMENT = 10000001;
ALTER TABLE farmer
ADD CONSTRAINT UFarmer_phonenum UNIQUE (phonenum);
ALTER TABLE farmer
ADD CONSTRAINT UFarmer_bvn UNIQUE (bvn);
ALTER TABLE farmer
ADD CONSTRAINT UFarmer_nim UNIQUE (nim);
ALTER TABLE farmer
ADD CONSTRAINT UFarmer_fml UNIQUE (first_name, middle_name, last_name);

CREATE TABLE farm (
 id INT(11) NOT NULL AUTO_INCREMENT,
 longi1 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 latit1 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 longi2 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 latit2 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 longi3 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 latit3 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 longi4 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 latit4 DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 area DECIMAL(16,2) DEFAULT '0.00' NOT NULL, 
 community varchar(25) NOT NULL,
 ward varchar(20) NOT NULL, 
 local_government varchar(25) NOT NULL,
 state_located varchar(25) NOT NULL,
 crop varchar(20) NOT NULL,
 area_planted DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 year_planted varchar(5) NOT NULL,
 harvest DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 net_worth DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 income DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 farmerid int NOT NULL,
 CONSTRAINT fk_fm_farmer FOREIGN KEY(farmerid) REFERENCES farmer(id),
 PRIMARY KEY (id)
);

ALTER TABLE farm AUTO_INCREMENT = 1000001;

ALTER TABLE farm
ADD CONSTRAINT UNQ_coords UNIQUE (longi1, latit1, longi2, latit2, longi3, latit3, longi4, latit4);

CREATE TABLE farminput (
 id INT(11) NOT NULL AUTO_INCREMENT,
 farm_input varchar(45) NOT NULL,
 input_cost DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 year_given varchar(5) NOT NULL,
 farmerid int NOT NULL,
 CONSTRAINT fk_fip_farmer FOREIGN KEY(farmerid) REFERENCES farmer(id),
 PRIMARY KEY (id)
);

ALTER TABLE farminput AUTO_INCREMENT = 1001;

CREATE TABLE fmgroup (
 id INT(11) NOT NULL AUTO_INCREMENT,
 group_name varchar(45) NOT NULL,
 address varchar(65) NOT NULL,
 ward varchar(20) NOT NULL,
 local_government varchar(25) NOT NULL,
 state_located varchar(25) NOT NULL,
 crops varchar(150) NOT NULL, 
 PRIMARY KEY (id)
);

ALTER TABLE fmgroup AUTO_INCREMENT = 101;

ALTER TABLE fmgroup
ADD CONSTRAINT UFmgroup_name UNIQUE (group_name);

ALTER TABLE fmgroup
ADD CONSTRAINT UFmgroup_address UNIQUE (address);

ALTER TABLE fmgroup
ADD CONSTRAINT UFmgroup_wls UNIQUE (ward, local_government, state_located);

CREATE TABLE officer (
 id INT(11) NOT NULL AUTO_INCREMENT,
 first_name varchar(20) NOT NULL,
 middle_name varchar(20) NOT NULL,
 last_name varchar(20) NOT NULL,
 office varchar(35) NOT NULL,
 phonenum varchar(15) NOT NULL,
 bvn varchar(20) NOT NULL,
 nim varchar(20) NOT NULL,
 home_address varchar(65) NOT NULL,
 groupid int NOT NULL,
 CONSTRAINT fk_of_group FOREIGN KEY(groupid) REFERENCES fmgroup(id),
 PRIMARY KEY (id)
);

ALTER TABLE officer AUTO_INCREMENT = 100001;
ALTER TABLE officer
ADD CONSTRAINT UOfficer_phonenum UNIQUE (phonenum);
ALTER TABLE officer
ADD CONSTRAINT UOfficer_bvn UNIQUE (bvn);
ALTER TABLE officer
ADD CONSTRAINT UOfficer_nim UNIQUE (nim);
ALTER TABLE officer
ADD CONSTRAINT UOfficer_fml UNIQUE (first_name, middle_name, last_name);

CREATE TABLE useraccount (
 id INT(11) NOT NULL AUTO_INCREMENT,
 username varchar(35) NOT NULL,
 password varchar(20) NOT NULL,
 phonenum varchar(15) NOT NULL,
 userrole varchar(30) NOT NULL,
 PRIMARY KEY (id)
);
ALTER TABLE useraccount AUTO_INCREMENT = 10101;
ALTER TABLE useraccount
ADD CONSTRAINT UUseracct_username UNIQUE (username);
ALTER TABLE useraccount
ADD CONSTRAINT UUseracct_password UNIQUE (password);
ALTER TABLE useraccount
ADD CONSTRAINT UUseracct_phonenum UNIQUE (phonenum);

INSERT INTO useraccount VALUES(null, 'asso officer','assocOF#$21','0731 342 9009','supperadmin');

CREATE TABLE audittrail (
 id INT(11) NOT NULL AUTO_INCREMENT,
 log_summary varchar(85) NOT NULL,
 log_detail varchar(215) NOT NULL,
 phonenum varchar(15) NOT NULL,
 PRIMARY KEY (id)
);

ALTER TABLE audittrail AUTO_INCREMENT = 100000001;
insert into audittrail (log_summary, log_detail, phonenum) values(?,?,?) 

CREATE TABLE aggregation (
 id INT(11) NOT NULL AUTO_INCREMENT,
 town varchar(25) NOT NULL,
 ward varchar(20) NOT NULL,
 local_government varchar(25) NOT NULL,
 state_located varchar(25) NOT NULL,
 groupid int NOT NULL,
 CONSTRAINT fk_agg_group FOREIGN KEY(groupid) REFERENCES fmgroup(id), 
 aggregation_level varchar(35) NOT NULL,
 PRIMARY KEY (id)
);

ALTER TABLE aggregation AUTO_INCREMENT = 10003;
ALTER TABLE aggregation
ADD CONSTRAINT UAggregation_twls UNIQUE (town, ward, local_government, state_located);

CREATE TABLE loan (
 id INT(11) NOT NULL AUTO_INCREMENT,
 description varchar(125) NOT NULL,
 amount_given DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 amount_paid DECIMAL(16,2) DEFAULT '0.00' NOT NULL,
 farmerid int NOT NULL,
 CONSTRAINT fk_ln_farmer FOREIGN KEY(farmerid) REFERENCES farmer(id),
 PRIMARY KEY (id)
);

ALTER TABLE loan AUTO_INCREMENT = 10000001;
ALTER TABLE loan
ADD CONSTRAINT LOAN_GIV_PAID CHECK (amount_paid <= amount_given);

CREATE TABLE contact (
 id INT(11) NOT NULL AUTO_INCREMENT,
 full_name varchar(30) NOT NULL,
 email varchar(25) NOT NULL,
 subject varchar(20) NOT NULL,
 message varchar(210) NOT NULL,
 PRIMARY KEY (id)
);

CREATE TABLE subscriber (
 id INT(11) NOT NULL AUTO_INCREMENT,
 email varchar(35) NOT NULL,
 status varchar(3) NOT NULL,
 PRIMARY KEY (id)
);