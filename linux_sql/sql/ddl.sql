DO $$
    BEGIN
    -- Check if the table exists
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_catalog = 'host_agent'
        AND table_schema = 'public'
        AND table_name = 'host_info'
    ) THEN
        -- Create the table if it does not exist
        CREATE TABLE PUBLIC.host_info  (
            -- Define table columns here
            id                      SERIAL NOT NULL,
            hostname                VARCHAR NOT NULL,
            cpu_number              INT2 NOT NULL,
            cpu_architecture        VARCHAR NOT NULL,
            cpu_model               VARCHAR NOT NULL,
            cpu_mhz                 FLOAT8 NOT NULL,
            l2_cache                INT4 NOT NULL,
            "timestamp"             TIMESTAMP NULL,
            total_mem               INT4 NULL,
            CONSTRAINT host_info_pk PRIMARY KEY (id),
            CONSTRAINT host_info_un UNIQUE (hostname)
        );

        -- Add any additional statements related to table creation if needed
    END IF;
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_catalog = 'host_agent'
          AND table_schema = 'public'
          AND table_name = 'host_usage'
    ) THEN
        -- Create the table if it does not exist
        CREATE TABLE PUBLIC.host_usage  (
            -- Define table columns here
            "timestamp"                         TIMESTAMP NOT NULL,
            host_id                             SERIAL NOT NULL,
            memory_free                         INT4 NOT NULL,
            cpu_idle                            INT2 NOT NULL,
            cpu_kernel                          INT2 NOT NULL,
            disk_io                             INT4 NOT NULL,
            disk_available                      INT4 NOT NULL,
            CONSTRAINT host_usage_host_info_fk  FOREIGN KEY (host_id) REFERENCES
            host_info(id)
        );

        -- Add any additional statements related to table creation if needed
    END IF;
END $$;

