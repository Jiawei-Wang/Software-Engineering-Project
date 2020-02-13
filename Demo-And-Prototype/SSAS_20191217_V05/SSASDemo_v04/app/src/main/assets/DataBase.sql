PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "User" (  "Userid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  "Username" TEXT,  "Password" TEXT,  "FullName" TEXT,  "AccountStatus" TEXT,  "DateCreated" DATE,  "UserPic" TEXT, Email TEXT);
INSERT INTO User VALUES(1,'Test','123456','Test','Active','2019-09-01',NULL,'yuchenyang96@gmail.com');
INSERT INTO User VALUES(2,'root','123456','Root','Active','2019-09-01',NULL,'yuchenyang96@gmail.com');
INSERT INTO User VALUES(3,'TeacherA','1234','TeacherA','Active','2019-09-01',NULL,'yuchenyang96@gmail.com');
INSERT INTO User VALUES(4,'TeacherB','1234','TeacherB','Active','2019-09-01',NULL,'yuchenyang96@gmail.com');
CREATE TABLE IF NOT EXISTS "Student" (  "Studentid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  "StudentName" TEXT,  "StudentEmail" TEXT,  "StudentStatus" TEXT,  "StudentPic" TEXT);
INSERT INTO Student VALUES(1,'Jerry','Jerry@tufts.edu','Active',NULL);
INSERT INTO Student VALUES(2,'Yuchen','yyang17@tufts.edu','Active',NULL);
CREATE TABLE IF NOT EXISTS "StudentCourse" (  "Choiceid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  "Studentid" INTEGER,  "Courseid" INTEGER,  CONSTRAINT "OnCourse" FOREIGN KEY ("Courseid") REFERENCES "Course" ("Courseid") ON DELETE NO ACTION ON UPDATE NO ACTION,  CONSTRAINT "OnStudent" FOREIGN KEY ("Studentid") REFERENCES "Student" ("Studentid") ON DELETE NO ACTION ON UPDATE NO ACTION);
INSERT INTO StudentCourse VALUES(1,1,1);
INSERT INTO StudentCourse VALUES(2,2,1);
INSERT INTO StudentCourse VALUES(3,1,2);
INSERT INTO StudentCourse VALUES(4,1,3);
INSERT INTO StudentCourse VALUES(5,1,4);
INSERT INTO StudentCourse VALUES(6,2,3);
INSERT INTO StudentCourse VALUES(7,2,4);
CREATE TABLE IF NOT EXISTS "Course"
(
	Courseid INTEGER not null
		primary key autoincrement,
	Teacherid INTEGER
		constraint OnUser
			references User,
	CourseName TEXT,
	OfferStatus TEXT,
	NumberOfClass INTEGER,
	CourseFullName TEXT
);
INSERT INTO Course VALUES(1,3,'Comp101','Active',32,'JAVA');
INSERT INTO Course VALUES(2,3,'Comp131','Active',16,'Artificial Intelligence');
INSERT INTO Course VALUES(3,4,'Comp170','Active',32,'Security');
INSERT INTO Course VALUES(4,1,'Test1','Active',16,'TestA');
INSERT INTO Course VALUES(5,1,'Test2','Active',32,'TestB');
INSERT INTO Course VALUES(6,1,'Test3','InActive',16,'TestC');
CREATE TABLE IF NOT EXISTS "StudentClass"
(
	Recordid INTEGER not null
		primary key autoincrement,
	Studentid INTEGER
		references Student,
	Classid INTEGER
		references Class,
	RecordTime DATE,
	AttendanceStatus TEXT
);
INSERT INTO StudentClass VALUES(1,1,1,'2019-12-01-13-12-11','Attend');
INSERT INTO StudentClass VALUES(2,1,3,'2019-12-01-13-12-11','Attend');
INSERT INTO StudentClass VALUES(3,1,4,'2019-12-01-13-12-11','Attend');
INSERT INTO StudentClass VALUES(4,1,5,'2019-12-01-13-12-11','Attend');
INSERT INTO StudentClass VALUES(5,1,6,'2019-12-01-13-12-11','Not Attend');
INSERT INTO StudentClass VALUES(6,2,3,'2019-12-01-13-12-11','Attend');
INSERT INTO StudentClass VALUES(7,2,4,'2019-12-01-13-12-11','Not Attend');
CREATE TABLE IF NOT EXISTS "Class"
(
	Classid INTEGER not null
		primary key autoincrement,
	Courseid INTEGER
		constraint OnCourse
			references Course,
	DateTime DATE,
	Classroom TEXT
);
INSERT INTO Class VALUES(1,1,'2019-12-01-18-00-00','SEC211');
INSERT INTO Class VALUES(3,4,'2019-12-01-18-00-00','SEC212');
INSERT INTO Class VALUES(4,4,'2019-12-03-18-00-00','SEC213');
INSERT INTO Class VALUES(5,4,'2019-12-05-18-00-00','SEC212');
INSERT INTO Class VALUES(6,5,'2019-12-04-18-00-00','SEC206');
DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('User',5);
INSERT INTO sqlite_sequence VALUES('Student',2);
INSERT INTO sqlite_sequence VALUES('StudentCourse',7);
INSERT INTO sqlite_sequence VALUES('Course',6);
INSERT INTO sqlite_sequence VALUES('StudentClass',7);
INSERT INTO sqlite_sequence VALUES('Class',6);
COMMIT;
