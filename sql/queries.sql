INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES ((SELECT MAX(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800);

UPDATE cd.facilities SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';

UPDATE cd.facilities SET membercost =
                             (SELECT membercost FROM cd.facilities WHERE name = 'Tennis Court 1')*1.1,
                         guestcost =
                             (SELECT guestcost FROM cd.facilities WHERE name = 'Tennis Court 1')*1.1
WHERE name = 'Tennis Court 2';

DELETE FROM cd.bookings;

DELETE FROM cd.members WHERE memid = 37;

SELECT facid, name, membercost, monthlymaintenance FROM cd.facilities
WHERE membercost <= monthlymaintenance/50 AND membercost > 0;

SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';

SELECT * FROM cd.facilities
WHERE facid IN (1,5);

SELECT memid, surname, firstname, joindate FROM cd.members
WHERE DATE(joindate) >= '2012-09-01';

SELECT surname FROM cd.members
UNION
SELECT name from cd.facilities;

SELECT starttime FROM cd.bookings INNER JOIN
                      cd.members ON cd.members.memid = cd.bookings.memid
WHERE cd.members.firstname = 'David' AND cd.members.surname = 'Farrell'

SELECT bks.starttime, fac.name FROM cd.bookings bks INNER JOIN
                                    cd.facilities fac ON fac.facid = bks.facid
WHERE fac.name LIKE '%Tennis Court%' AND DATE(bks.starttime) = '2012-09-21'
ORDER BY bks.starttime ASC;

SELECT mem.firstname AS memfname, mem.surname AS memsname,
       rec.firstname AS recfname, rec.surname AS recsname FROM cd.members mem
                                                                   LEFT OUTER JOIN cd.members rec ON rec.memid = mem.recommendedby
ORDER BY mem.surname, mem.firstname;

SELECT DISTINCT rec.firstname, rec.surname FROM cd.members mem
                                                    INNER JOIN cd.members rec ON rec.memid = mem.recommendedby
ORDER BY rec.surname, rec.firstname;

SELECT DISTINCT CONCAT(mem.firstname, ' ', mem.surname) AS member,
                (SELECT CONCAT(rec.firstname, ' ', rec.surname) AS recommender
                 FROM cd.members rec
                 WHERE mem.recommendedby = rec.memid
                )
FROM cd.members mem
ORDER BY member;

SELECT recommendedby, COUNT(recommendedby) AS count FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

SELECT facid, SUM(slots) AS "Total Slots" FROM cd.bookings
GROUP BY facid
ORDER BY facid;

SELECT facid, SUM(slots) AS "Total Slots" FROM cd.bookings
WHERE DATE(starttime) >= '2012-09-01' AND DATE(starttime) <= '2012-09-30'
GROUP BY facid
ORDER BY "Total Slots";

SELECT facid, EXTRACT(month FROM starttime) AS month, SUM(slots) AS "Total Slots" FROM cd.bookings
WHERE DATE(starttime) >= '2012-01-01' AND DATE(starttime) <= '2012-12-31'
GROUP BY facid, month
ORDER BY facid, month;

SELECT COUNT(DISTINCT memid) FROM cd.bookings;

SELECT DISTINCT mem.surname, mem.firstname, mem.memid, MIN(bks.starttime)
FROM cd.members mem
         INNER JOIN cd.bookings bks ON mem.memid = bks.memid
WHERE DATE(bks.starttime) >= '2012-09-01'
GROUP BY mem.memid
ORDER BY mem.memid;

SELECT COUNT(*) OVER(), firstname, surname FROM cd.members
GROUP BY firstname, surname, memid
ORDER BY memid

SELECT ROW_NUMBER() OVER(ORDER BY joindate) AS row_number , firstname, surname FROM cd.members
GROUP BY firstname, surname, joindate
ORDER BY joindate

SELECT facid, total FROM (
                             SELECT facid, SUM(slots) total, RANK() over (ORDER BY SUM(slots) desc) rank
                             FROM cd.bookings
                             GROUP BY facid
                         ) AS ranked
WHERE rank = 1

SELECT CONCAT(surname, ', ', firstname) FROM cd.members;
-- OR this way
SELECT surname || ', ' || firstname AS name FROM cd.members;

SELECT memid, telephone FROM cd.members
WHERE telephone LIKE '(%)%';
-- OR this way
select memid, telephone from cd.members where telephone ~ '[()]';

SELECT SUBSTR(surname, 1, 1), COUNT(SUBSTR(surname, 1, 1)) FROM cd.members
GROUP BY SUBSTR(surname, 1, 1)
ORDER BY SUBSTR(surname, 1, 1);
