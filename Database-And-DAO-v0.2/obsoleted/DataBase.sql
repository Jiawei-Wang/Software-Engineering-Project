PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "User" (
  "Userid" INTEGER NOT NULL ON CONFLICT FAIL,
  "Username" TEXT,
  "Password" TEXT,
  "Full_Name" TEXT,
  "Email_Address" TEXT,
  "Account_Status" TEXT,
  "Date_Created" DATE,
  CONSTRAINT "Userid" PRIMARY KEY ("Userid")
);
INSERT INTO User VALUES(1,'root','123456','root','root@root.com','Active','2019/11/25');
CREATE TABLE IF NOT EXISTS "Class" (
  "Classid" INTEGER NOT NULL ON CONFLICT FAIL,
  "Teacherid" INTEGER,
  "Class_Name" TEXT,
  "Offer_Status" TEXT,
  "Number_Of_Offering" INTEGER,
  CONSTRAINT "Classid" PRIMARY KEY ("Classid"),
  CONSTRAINT "Class-User" FOREIGN KEY ("Teacherid") REFERENCES "User" ("Userid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO Class VALUES(1,1,'COMP101','None',1);
CREATE TABLE IF NOT EXISTS "Student" (
  "Studentid" INTEGER NOT NULL,
  "Student_Name" TEXT,
  "Student_Email" TEXT,
  "Student_Status" TEXT,
  "Payment_Info" TEXT,
  PRIMARY KEY ("Studentid")
);
INSERT INTO Student VALUES(1,'Takanashi Rikka','rikka@tufts.edu','Nice!','Cash');
CREATE TABLE IF NOT EXISTS "ClassOffering" (
  "Class_Offering_ID" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Classid" INTEGER,
  "Date_Time_Offering" TEXT,
  "Number_Of_Student" INTEGER,
  "Classroom" TEXT,
  CONSTRAINT "ClassOffering-Class" FOREIGN KEY ("Classid") REFERENCES "Class" ("Classid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
INSERT INTO ClassOffering VALUES(13,1,'Tuesday & Thursday 18:00',20,'SEC211');
INSERT INTO ClassOffering VALUES(14,1,'Tuesday & Thursday 18:00',20,'SEC211');
CREATE TABLE IF NOT EXISTS "ClassChosenByStudent" (
  "Choiceid" INTEGER NOT NULL,
  "Studentid" INTEGER,
  "Offeringid" INTEGER,
  PRIMARY KEY ("Choiceid"),
  CONSTRAINT "ClassChosenByStudent-On Offering" FOREIGN KEY ("Offeringid") REFERENCES "ClassOffering" ("Class_Offering_ID") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "ClassChosenByStudent-On Student" FOREIGN KEY ("Studentid") REFERENCES "Student" ("Studentid") ON DELETE NO ACTION ON UPDATE NO ACTION
);
CREATE TABLE IF NOT EXISTS "Student_Attendance" (
  "Attendanceid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "Studentid" INTEGER,
  "Offeringid" TEXT,
  "Record_Date" TEXT,
  "Status" TEXT,
  CONSTRAINT "Attendance-On Student" FOREIGN KEY ("Studentid") REFERENCES "Student" ("Studentid") ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT "Attendance-On Offering" FOREIGN KEY ("Offeringid") REFERENCES "ClassOffering" ("Class_Offering_ID") ON DELETE NO ACTION ON UPDATE NO ACTION
);
DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('ClassOffering',14);
INSERT INTO sqlite_sequence VALUES('Student_Attendance',0);
COMMIT;
