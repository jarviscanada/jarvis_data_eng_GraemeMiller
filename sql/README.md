# Introduction

# SQL Queries

###### Table Setup (DDL)
````sql
CREATE TABLE members (
  memid integer NOT NULL, 
  surname varchar(200) NOT NULL, 
  firstname varchar(200) NOT NULL, 
  address varchar(300) NOT NULL, 
  zipcode integer NOT NULL, 
  telephone varchar(20) NOT NULL, 
  recommendedby integer, 
  joindate timestamp NOT NULL, 
  CONSTRAINT members_PK PRIMARY KEY (memid), 
  CONSTRAINT recommendedby_FK FOREIGN KEY (recommendedby) REFERENCES members (memid) ON DELETE 
  SET 
    NULL
);
CREATE TABLE bookings (
  bookid integer NOT NULL, 
  facid integer NOT NULL, 
  memid integer NOT NULL, 
  starttime timestamp NOT NULL, 
  slots integer NOT NULL, 
  CONSTRAINT bookings_PK PRIMARY KEY (bookid), 
  CONSTRAINT mem_FK FOREIGN KEY (memid) REFERENCES members (memid), 
  CONSTRAINT fac_FK FOREIGN KEY (facid) REFERENCES facilities (facid)
);
CREATE TABLE facilities (
  facid integer NOT NULL, 
  name varchar(100) NOT NULL, 
  membercost numeric NOT NULL, 
  guestcost numeric NOT NULL, 
  initialoutlay numeric NOT NULL, 
  monthlymaintenence numeric NOT NULL, 
  CONSTRAINT facilities_PK PRIMARY KEY (facid)
);

````

###### Question 1: Insert some data into a table
````sql
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);
````


###### Questions 2: Insert calculated data into a table
````sql
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES ((SELECT MAX(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800);
````

###### Questions 3: Update some existing data
````sql
UPDATE cd.facilities SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';
````

###### Questions 4: Update a row based on the contents of another row
````sql
UPDATE cd.facilities SET membercost = 
(SELECT membercost FROM cd.facilities 
                   WHERE name = 'Tennis Court 1')*1.1,
guestcost = 
(SELECT guestcost FROM cd.facilities 
                  WHERE name = 'Tennis Court 1')*1.1
WHERE name = 'Tennis Court 2';
````

###### Questions 5: Delete all bookings
````sql
DELETE FROM cd.bookings;
````

###### Questions 6: Delete a member from the cd.members table
````sql
DELETE FROM cd.members WHERE memid = 37;
````

###### Questions 7: Control which rows are retrieved - part 2
````sql
SELECT facid, name, membercost, monthlymaintenance 
FROM cd.facilities
WHERE membercost <= monthlymaintenance/50 AND membercost > 0;
````

###### Questions 8: Basic string searches
````sql
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';
````

###### Questions 9: Matching against multiple possible values
````sql
SELECT * FROM cd.facilities
WHERE facid IN (1,5);
````

###### Questions 10: Working with dates
````sql
SELECT memid, surname, firstname, joindate FROM cd.members
WHERE DATE(joindate) >= '2012-09-01';
````

###### Questions 11: Combining results from multiple queries
````sql
SELECT surname FROM cd.members 
UNION
SELECT name from cd.facilities;
````

###### Questions 12: Retrieve the start times of members' bookings
````sql
SELECT starttime FROM cd.bookings INNER JOIN 
cd.members ON cd.members.memid = cd.bookings.memid
WHERE cd.members.firstname = 'David' AND 
      cd.members.surname = 'Farrell'
````

###### Questions 13: Work out the start times of bookings for tennis courts
````sql
SELECT bks.starttime, fac.name FROM cd.bookings bks INNER JOIN 
cd.facilities fac ON fac.facid = bks.facid
WHERE fac.name LIKE '%Tennis Court%' AND 
      DATE(bks.starttime) = '2012-09-21'
ORDER BY bks.starttime ASC;
````

###### Questions 14: Produce a list of all members, along with their recommender
````sql
SELECT mem.firstname AS memfname, mem.surname AS memsname, 
rec.firstname AS recfname, rec.surname AS recsname FROM cd.members mem
LEFT OUTER JOIN cd.members rec ON rec.memid = mem.recommendedby
ORDER BY mem.surname, mem.firstname;
````

###### Questions 15: Produce a list of all members who have recommended another member
````sql
SELECT DISTINCT rec.firstname, rec.surname FROM cd.members mem
INNER JOIN cd.members rec ON rec.memid = mem.recommendedby
ORDER BY rec.surname, rec.firstname;
````

###### Questions 16: Produce a list of all members, along with their recommender, using no joins.
````sql
SELECT DISTINCT CONCAT(mem.firstname, ' ', mem.surname) AS member, 
	(SELECT CONCAT(rec.firstname, ' ', rec.surname) AS recommender
	 	FROM cd.members rec
		WHERE mem.recommendedby = rec.memid
	)
	FROM cd.members mem
ORDER BY member;
````

###### Questions 17: Count the number of recommendations each member makes.
````sql
SELECT recommendedby, COUNT(recommendedby) AS count FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
````

###### Questions 18: List the total slots booked per facility
````sql
SELECT facid, SUM(slots) AS "Total Slots" FROM cd.bookings
GROUP BY facid
ORDER BY facid;
````

###### Questions 19: List the total slots booked per facility in a given month
````sql
SELECT facid, SUM(slots) AS "Total Slots" FROM cd.bookings
WHERE DATE(starttime) >= '2012-09-01' AND DATE(starttime) <= '2012-09-30'
GROUP BY facid
ORDER BY "Total Slots";
````

###### Questions 20: List the total slots booked per facility per month
````sql
SELECT facid, EXTRACT(month FROM starttime) AS month, SUM(slots) AS "Total Slots" FROM cd.bookings
	WHERE DATE(starttime) >= '2012-01-01' AND DATE(starttime) <= '2012-12-31'
	GROUP BY facid, month
ORDER BY facid, month;
````

###### Questions 21: Find the count of members who have made at least one booking
````sql
SELECT COUNT(DISTINCT memid) FROM cd.bookings;
````

###### Questions 22: List each member's first booking after September 1st 2012
````sql
SELECT DISTINCT mem.surname, mem.firstname, mem.memid, MIN(bks.starttime)
FROM cd.members mem 
INNER JOIN cd.bookings bks ON mem.memid = bks.memid
WHERE DATE(bks.starttime) >= '2012-09-01'
GROUP BY mem.memid
ORDER BY mem.memid;
````

###### Questions 23: Produce a list of member names, with each row containing the total member count
````sql
SELECT COUNT(*) OVER(), firstname, surname FROM cd.members
GROUP BY firstname, surname, memid
ORDER BY memid
````

###### Questions 24: Produce a numbered list of members
````sql
SELECT ROW_NUMBER() OVER(ORDER BY joindate) AS row_number , firstname, surname FROM cd.members
GROUP BY firstname, surname, joindate
ORDER BY joindate
````

###### Questions 25: Output the facility id that has the highest number of slots booked, again
````sql
SELECT facid, total FROM (
	SELECT facid, SUM(slots) total, RANK() over (ORDER BY SUM(slots) desc) rank
        	FROM cd.bookings
		GROUP BY facid
	) AS ranked
	WHERE rank = 1 
````

###### Questions 26: Format the names of members
````sql
SELECT CONCAT(surname, ', ', firstname) FROM cd.members;
-- OR this way
SELECT surname || ', ' || firstname AS name FROM cd.members;
````

###### Questions 27: Find telephone numbers with parentheses
````sql
SELECT memid, telephone FROM cd.members
WHERE telephone LIKE '(%)%';
-- OR this way
select memid, telephone from cd.members where telephone ~ '[()]';  
````

###### Questions 28: Count the number of members whose surname starts with each letter of the alphabet
````sql
SELECT SUBSTR(surname, 1, 1), COUNT(SUBSTR(surname, 1, 1)) FROM cd.members
GROUP BY SUBSTR(surname, 1, 1)
ORDER BY SUBSTR(surname, 1, 1);
````
