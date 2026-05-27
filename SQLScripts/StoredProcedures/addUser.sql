-------- function  to add user

CREATE OR REPLACE FUNCTION insert_user(
p_user_firstname VARCHAR,
    p_user_middlename VARCHAR,
    p_user_lastname VARCHAR,
    p_user_email VARCHAR,
    p_user_contactnumber NUMERIC,
    p_user_gender VARCHAR,
    p_user_dob DATE,
    p_user_address VARCHAR,
    p_admin_key INTEGER
)
RETURNS BOOLEAN
AS $$
BEGIN
    INSERT INTO table_user(
user_firstname,
        user_middlename,
        user_lastname,
        user_email,
        user_contactnumber,
        user_gender,
        user_dob,
        user_address,
        admin_key
)
VALUES(

 p_user_firstname,
        p_user_middlename,
        p_user_lastname,
        p_user_email,
        p_user_contactnumber,
        p_user_gender,
        p_user_dob,
        p_user_address,
        p_admin_key
);
RETURN TRUE;
EXCEPTION
     WHEN OTHERS THEN
    RETURN FALSE;
END;
$$
LANGUAGE plpgsql;