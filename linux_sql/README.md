# Introduction
This projects goal is to assist the Jarvis Linux Cluster Administration (LCA) team in managing a Linux server cluster consisting of 10 nodes/servers. Using Docker containers, the project simplifies access to PostgreSQL, allowing for the creation of a centralized database to store vital information on the hardware specifications and usage data of each node. By creating scheduled scripts executed through crontab, the project ensures consistent and automated data collection, enabling the LCA team to monitor the usage data of the server cluster effectively. This project?s approach not only collects data efficiently allowing for enhanced performance and reliability of the Linux server cluster.

# Quick Start

```shell
#Starting a psql instance using psql_docker.sh
#Create a new docker container if it doesn't exist
scripts/psql_docker.sh create yourUsername yourPassword
#Run when the container exists and is not running
scripts/psql_docker.sh start
#Run when the container exists and is running
scripts/psql_docker.sh stop

#Create the initial tables to prepare the database
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql

#Insert hardware specs into DB
./scripts/host_info.sh "localhost" 5432 "host_agent" "yourUsername" "yourPassword"
./scripts/host_usage.sh "localhost" 5432 "host_agent" "yourUsername" "yourPassword"

#Setup crontab for automated running
* * * * * bash /~/linux_sql/scripts/host_usage.sh localhost 5432 host_agent temp password > /tmp/host_usage.log
```

# Implementation
Using docker containers allowed the project to have easy access to the PostgreSQL and create databases. We then used bash scripts to setup and apply changes to the tables for the database. 

## Architecture
![clusterDiagram](https://github.com/jarviscanada/jarvis_data_eng_GraemeMiller/assets/61242097/6342d671-296f-419b-afd9-abacfea3e0a2)

## Database Modeling
### Host Info
```markdown
| Column Name      | Data Type | Constraints | Description                     |
|------------------|-----------|-------------|---------------------------------|
| id               | SERIAL    | NOT NULL    | Unique identifier for each host |
| hostname         | VARCHAR   | NOT NULL    | Name of the host device         |
| cpu_number       | INT2      | NOT NULL    | Number of CPUs                  |
| cpu_architecture | VARCHAR   | NOT NULL    | CPU Architecture name           |
| cpu_model        | VARCHAR   | NOT NULL    | CPU model name                  |
| cpu_mhz          | FLOAT8    | NOT NULL    | Clock speed of the CPU in (MHz) |
| l2_cache         | INT4      | NOT NULL    | l2 cache size in (KiB)          |
| timestamp        | TIMESTAMP | NOT NULL    | Timestamp of the data collected |
| total_mem        | INT4      | NOT NULL    | Total memory available in (MB)  |
```
### Host Usage
```markdown
| Column Name    | Data Type | Constraints | Description                            |
|----------------|-----------|-------------|----------------------------------------|
| timestamp      | TIMESTAMP | NOT NULL    | Timestamp of the data collected        |
| host_id        | SERIAL    | NOT NULL    | Unique identifier for each host        |
| memory_free    | INT4      | NOT NULL    | Available memory on the device         |
| cpu_idle       | INT2      | NOT NULL    | Percentage of CPU idle time            |
| cpu_kernel     | INT2      | NOT NULL    | Percentage of CPU kernel time          |
| disk_io        | INT4      | NOT NULL    | Number of disk I/O operations          |
| disk_available | INT4      | NOT NULL    | Amount of available disk space in (MB) |
```

# Test
When testing this project I created sample versions of the database to ensure that each set of scripts could properly 
run and complete all the potential entries that can be done with this project.
Once verified that the project generates correct outputs and effectively triggers error alerts, I can confirm its 
completion.

# Deployment
The app is deployed with all files available here on GitHub. After accessing the files and utilizing a docker container
to access PostgreSQL you can set up the initial database and then initialize crontab to automate data collection.

# Improvements
To improve this product I would like to do tests with full systems to see how they all handle the data collection and 
to see how useful the data is to view. Another thing I would improve is the data view. I think there could be a benefit
to handling the data and displaying it in charts or other graphics for easier assessment. One final improvement I could
make is streamlining the initialization process. With more time to assess how understandable this product could be for
new users I believe that there could be ways of ensuring it is more understandable.

