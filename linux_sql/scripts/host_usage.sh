#! /bin/sh

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

export PGPASSWORD=$psql_password

if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

memory_free=$(echo "$vmstat_mb" | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk -v col="15" '{print $col}')
cpu_kernel=$(echo "$vmstat_mb"| tail -1 | awk -v col="14" '{print $col}')
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -BM / | tail -1 | awk -v col="4" '{print $col}' | sed 's/[^0-9]*//g')
timestamp=$(vmstat -t | tail -1 | awk '{print $(NF-1), $NF}')
date=$(date -d "$timestamp" +"%Y-%m-%d %H:%M:%S")

query="SELECT id FROM host_info WHERE hostname = '$hostname'"
id=$(psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$query")

id_q=$(echo "$id" | tail -2 | head -1)

insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$date', $id_q, '$memory_free', '$cpu_idle', $cpu_kernel, '$disk_io', $disk_available);"
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_stmt"