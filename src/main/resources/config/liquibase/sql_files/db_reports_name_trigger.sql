CREATE OR REPLACE FUNCTION reportLowerCase() RETURNS trigger
    LANGUAGE PLPGSQL AS
$$BEGIN
    new."report_name" := initcap(new."report_name");
    RETURN NEW;
END;$$;

CREATE TRIGGER lowercase_name
    BEFORE INSERT
    ON t_report
    FOR EACH ROW EXECUTE PROCEDURE reportLowerCase();
