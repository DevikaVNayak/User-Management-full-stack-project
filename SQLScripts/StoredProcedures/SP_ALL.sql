-- function  to add user

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

-- function  to add admin

CREATE OR REPLACE FUNCTION insert_admin(
    p_admin_id VARCHAR,
    p_admin_firstname VARCHAR,
    p_admin_middlename VARCHAR,
    p_admin_lastname VARCHAR,
    p_admin_email VARCHAR,
    p_admin_password VARCHAR,
    p_admin_contact NUMERIC,
    p_admin_address VARCHAR,
    p_admin_gender VARCHAR,
    p_admin_dob DATE,
    p_admin_question VARCHAR,
    p_admin_answer VARCHAR
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO table_admin(
        admin_id,
        admin_firstname,
        admin_middlename,
        admin_lastname,
        admin_email,
        admin_password,
        admin_contact,
        admin_address,
        admin_gender,
        admin_dob,
        admin_question,
        admin_answer
    )
    VALUES (
        p_admin_id,
        p_admin_firstname,
        p_admin_middlename,
        p_admin_lastname,
        p_admin_email,
        p_admin_password,
        p_admin_contact,
        p_admin_address,
        p_admin_gender,
        p_admin_dob,
        p_admin_question,
        p_admin_answer
    );

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END;
$$;


-- TO Create users_filter_search_sort Stored Procedure

CREATE OR REPLACE FUNCTION filter_user_details(
    p_admin_id INTEGER,
    p_fname VARCHAR, p_fcondition VARCHAR,
    p_lname VARCHAR, p_lcondition VARCHAR,
    p_email VARCHAR, p_econdition VARCHAR,
    p_phone VARCHAR, p_pcondition VARCHAR,
    p_gender VARCHAR, p_gcondition VARCHAR,
    p_address VARCHAR, p_acondition VARCHAR,
    p_limit INTEGER, p_offset INTEGER
)
RETURNS SETOF table_user AS $$
DECLARE
    allsearch TEXT;
    searchQuery TEXT := '';
BEGIN
    -- Base selection
    allsearch := 'SELECT * FROM table_user WHERE admin_key = '  || p_admin_id;

    -- 1. First Name Condition Assignment
    IF p_fname <> '' THEN
        IF p_fcondition = 'starts' THEN
            searchQuery := searchQuery || ' AND user_firstname ILIKE ' || quote_literal(p_fname || '%');
        ELSIF p_fcondition = 'ends' THEN
            searchQuery := searchQuery || ' AND user_firstname ILIKE ' || quote_literal('%' || p_fname);
        ELSIF p_fcondition = 'equals' THEN
            searchQuery := searchQuery || ' AND user_firstname ILIKE ' || quote_literal(p_fname);
        ELSE -- Default to 'contains'
            searchQuery := searchQuery || ' AND user_firstname ILIKE ' || quote_literal('%' || p_fname || '%');
        END IF;
    END IF;

    -- 2. Last Name Condition Assignment
    IF p_lname <> '' THEN
        IF p_lcondition = 'starts' THEN
            searchQuery := searchQuery || ' AND user_lastname ILIKE ' || quote_literal(p_lname || '%');
        ELSIF p_lcondition = 'ends' THEN
            searchQuery := searchQuery || ' AND user_lastname ILIKE ' || quote_literal('%' || p_lname);
        ELSIF p_lcondition = 'equals' THEN
            searchQuery := searchQuery || ' AND user_lastname ILIKE ' || quote_literal(p_lname);
        ELSE
            searchQuery := searchQuery || ' AND user_lastname ILIKE ' || quote_literal('%' || p_lname || '%');
        END IF;
    END IF;

    -- 3. Email Condition Assignment
    IF p_email <> '' THEN
        IF p_econdition = 'starts' THEN
            searchQuery := searchQuery || ' AND user_email ILIKE ' || quote_literal(p_email || '%');
        ELSIF p_econdition = 'equals' THEN
            searchQuery := searchQuery || ' AND user_email ILIKE ' || quote_literal(p_email);
        ELSIF p_econdition = 'ends' THEN
            searchQuery := searchQuery || ' AND user_email ILIKE ' || quote_literal('%'||p_email);
        ELSE
            searchQuery := searchQuery || ' AND user_email ILIKE ' || quote_literal('%' || p_email || '%');
        END IF;
    END IF;

    -- 4. Contact/Phone Condition Assignment
    IF p_phone <> '' THEN
        IF p_pcondition = 'starts' THEN
            searchQuery := searchQuery || ' AND user_contactnumber::TEXT LIKE ' || quote_literal(p_phone || '%');
        ELSIF p_pcondition = 'equals' THEN
            searchQuery := searchQuery || ' AND user_contactnumber::TEXT LIKE ' || quote_literal(p_phone);
        ELSIF p_pcondition = 'ends' THEN
            searchQuery := searchQuery || ' AND user_contactnumber::TEXT LIKE ' || quote_literal('%'||p_phone);
        ELSE
            searchQuery := searchQuery || ' AND user_contactnumber::TEXT LIKE ' || quote_literal('%' || p_phone || '%');
        END IF;
    END IF;

    -- 5. Gender Condition Assignment
    -- Usually gender is a strict match (Equals), but we check gcondition just in case
    IF p_gender <> '' THEN
        IF p_gcondition = 'contains' THEN
            searchQuery := searchQuery || ' AND user_gender ILIKE ' || quote_literal('%' || p_gender || '%');
        ELSE
            searchQuery := searchQuery || ' AND user_gender = ' || quote_literal(p_gender);
        END IF;
    END IF;

    -- 6. Address Condition Assignment
    IF p_address <> '' THEN
        IF p_acondition = 'starts' THEN
            searchQuery := searchQuery || ' AND user_address ILIKE ' || quote_literal(p_address || '%');
        ELSIF p_acondition = 'equals' THEN
            searchQuery := searchQuery || ' AND user_address ILIKE ' || quote_literal(p_address);
        ELSIF p_acondition = 'ends' THEN
            searchQuery := searchQuery || ' AND user_address ILIKE ' || quote_literal('%'||p_address);
        ELSE
            searchQuery := searchQuery || ' AND user_address ILIKE ' || quote_literal('%' || p_address || '%');
        END IF;
    END IF;

    -- Finalize query with Sorting and Pagination
    searchQuery := searchQuery || ' ORDER BY user_key DESC LIMIT ' || p_limit || ' OFFSET ' || p_offset || ';';
RAISE NOTICE 'Name is  searchQuery%', searchQuery;
    RETURN QUERY EXECUTE allsearch || searchQuery;
END;
$$ LANGUAGE plpgsql;


-- TO Get Users count, users_filter_search_count_nooffset Stored Procedure -

CREATE OR REPLACE FUNCTION get_user_count(
    p_admin_id INT,
    p_fname TEXT, p_fcondition TEXT,
    p_lname TEXT, p_lcondition TEXT,
    p_email TEXT, p_econdition TEXT,
    p_phone TEXT, p_pcondition TEXT,
    p_gender TEXT, p_gcondition TEXT,
    p_address TEXT, p_acondition TEXT
)
RETURNS INTEGER
LANGUAGE plpgsql
AS $$
DECLARE
    query TEXT;
    result INTEGER;
BEGIN
    query := 'SELECT COUNT(*) FROM table_user WHERE admin_key = ' || p_admin_id;

    -- Firstname
    IF p_fname <> '' THEN
        IF p_fcondition = 'starts' THEN
            query := query || ' AND user_firstname ILIKE ' || quote_literal(p_fname || '%');
        ELSIF p_fcondition = 'ends' THEN
            query := query || ' AND user_firstname ILIKE ' || quote_literal('%' || p_fname);
        ELSIF p_fcondition = 'equals' THEN
            query := query || ' AND user_firstname ILIKE ' || quote_literal(p_fname);
        ELSE
            query := query || ' AND user_firstname ILIKE ' || quote_literal('%' || p_fname || '%');
        END IF;
    END IF;

    -- Lastname
    IF p_lname <> '' THEN
        IF p_lcondition = 'starts' THEN
            query := query || ' AND user_lastname ILIKE ' || quote_literal(p_lname || '%');
        ELSIF p_lcondition = 'ends' THEN
            query := query || ' AND user_lastname ILIKE ' || quote_literal('%' || p_lname);
        ELSIF p_lcondition = 'equals' THEN
            query := query || ' AND user_lastname ILIKE ' || quote_literal(p_lname);
        ELSE
            query := query || ' AND user_lastname ILIKE ' || quote_literal('%' || p_lname || '%');
        END IF;
    END IF;

    -- Email
    IF p_email <> '' THEN
        IF p_econdition = 'starts' THEN
            query := query || ' AND user_email ILIKE ' || quote_literal(p_email || '%');
        ELSIF p_econdition = 'ends' THEN
            query := query || ' AND user_email ILIKE ' || quote_literal('%' || p_email);
        ELSIF p_econdition = 'equals' THEN
            query := query || ' AND user_email ILIKE ' || quote_literal(p_email);
        ELSE
            query := query || ' AND user_email ILIKE ' || quote_literal('%' || p_email || '%');
        END IF;
    END IF;

    -- Phone
    IF p_phone <> '' THEN
        IF p_pcondition = 'starts' THEN
            query := query || ' AND user_contactnumber::text LIKE ' || quote_literal(p_phone || '%');
        ELSIF p_pcondition = 'ends' THEN
            query := query || ' AND user_contactnumber::text LIKE ' || quote_literal('%' || p_phone);
        ELSIF p_pcondition = 'equals' THEN
            query := query || ' AND user_contactnumber::text LIKE ' || quote_literal(p_phone);
        ELSE
            query := query || ' AND user_contactnumber::text LIKE ' || quote_literal('%' || p_phone || '%');
        END IF;
    END IF;

    -- Gender
    IF p_gender <> '' THEN
        IF p_gcondition = 'contains' THEN
            query := query || ' AND user_gender ILIKE ' || quote_literal('%' || p_gender || '%');
        ELSE
            query := query || ' AND user_gender = ' || quote_literal(p_gender);
        END IF;
    END IF;

    -- Address
    IF p_address IS NOT NULL AND p_address <> '' THEN
        IF p_acondition = 'starts' THEN
            query := query || ' AND user_address ILIKE ' || quote_literal(p_address || '%');
        ELSIF p_acondition = 'ends' THEN
            query := query || ' AND user_address ILIKE ' || quote_literal('%' || p_address);
        ELSIF p_acondition = 'equals' THEN
            query := query || ' AND user_address ILIKE ' || quote_literal(p_address);
        ELSE
            query := query || ' AND user_address ILIKE ' || quote_literal('%' || p_address || '%');
        END IF;
    END IF;

    EXECUTE query INTO result;
    RETURN result;
END;
$$;