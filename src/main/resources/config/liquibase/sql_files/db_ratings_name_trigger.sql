CREATE OR REPLACE FUNCTION lowerCase() RETURNS trigger
    LANGUAGE PLPGSQL AS
$$BEGIN
    new."data_source_name" := initcap(new."data_source_name");
    RETURN NEW;
END;$$;

CREATE TRIGGER lowercase_name
    BEFORE INSERT
    ON t_data_source
    FOR EACH ROW EXECUTE PROCEDURE lowerCase();
