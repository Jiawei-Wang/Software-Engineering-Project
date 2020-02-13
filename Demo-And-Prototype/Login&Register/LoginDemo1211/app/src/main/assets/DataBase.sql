PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "User" (
  "Userid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Username" TEXT,
  "Password" TEXT,
  "FullName" TEXT,
  "AccountStatus" TEXT,
  "DateCreated" DATE,
  "UserPic" TEXT
);
INSERT INTO User VALUES(1,'Test','123456','Test','Active','2019-09-01',NULL);
INSERT INTO User VALUES(2,'root','123456','Root','Active','2019-09-01',NULL);
INSERT INTO User VALUES(3,'TeacherA','1234','TeacherA','Active','2019-09-01',NULL);
INSERT INTO User VALUES(4,'TeacherB','1234','TeacherB','Active','2019-09-01',NULL);
CREATE TABLE IF NOT EXISTS "Student" (
  "Studentid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "StudentName" TEXT,
  "StudentEmail" TEXT,
  "StudentStatus" TEXT,
  "StudentPic" TEXT
);
INSERT INTO Student VALUES(1,'Jerry','Jerry@tufts.edu','Active',NULL);
INSERT INTO Student VALUES(2,'Yuchen','yyang17@tufts.edu','Active',NULL);
CREATE TABLE IF NOT EXISTS "Course" (
  "Courseid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Teacherid" INTEGER,
  "CourseName" TEXT,
  "OfferStatus" TEXT,
  "NumberOfClass" INTEGER,
  "NumberOfStudent" INTEGER,
  CONSTRAINT "OnUser" FOREIGN KEY ("Teacherid") REFERENCES "User" ("Userid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO Course VALUES(1,3,'Comp101','Active',32,NULL);
INSERT INTO Course VALUES(2,3,'Comp131','Active',16,NULL);
INSERT INTO Course VALUES(3,4,'Comp170','Active',32,NULL);
CREATE TABLE IF NOT EXISTS "Student-Course" (
  "Choiceid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Studentid" INTEGER,
  "Courseid" INTEGER,
  CONSTRAINT "OnCourse" FOREIGN KEY ("Courseid") REFERENCES "Course" ("Courseid") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "OnStudent" FOREIGN KEY ("Studentid") REFERENCES "Student" ("Studentid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO "Student-Course" VALUES(1,1,1);
INSERT INTO "Student-Course" VALUES(2,2,1);
INSERT INTO "Student-Course" VALUES(3,1,2);
INSERT INTO "Student-Course" VALUES(4,1,3);
CREATE TABLE IF NOT EXISTS "Class" (
  "Classid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Courseid" INTEGER,
  "DateTime" DATE,
  "Classroom" TEXT,
  CONSTRAINT "OnCourse" FOREIGN KEY ("Courseid") REFERENCES "Course" ("Courseid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO Class VALUES(1,1,'2019-12-01-18-00-00','SEC211');
CREATE TABLE IF NOT EXISTS "Student-Class" (
  "Recordid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Studentid" INTEGER,
  "Classid" INTEGER,
  "AttendanceRecorded" BOOLEAN,
  "RecordTime" DATE,
  "AttendanceStatus" TEXT,
  CONSTRAINT "onStudent" FOREIGN KEY ("Studentid") REFERENCES "Student" ("Studentid") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "onClass" FOREIGN KEY ("Classid") REFERENCES "Class" ("Classid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO "Student-Class" VALUES(1,1,1,'True','2019-12-01-13-12-11','Attend');
DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('User',4);
INSERT INTO sqlite_sequence VALUES('Student',2);
INSERT INTO sqlite_sequence VALUES('Course','3');
INSERT INTO sqlite_sequence VALUES('Student-Course',4);
INSERT INTO sqlite_sequence VALUES('Class',2);
INSERT INTO sqlite_sequence VALUES('Student-Class',1);
COMMIT;
