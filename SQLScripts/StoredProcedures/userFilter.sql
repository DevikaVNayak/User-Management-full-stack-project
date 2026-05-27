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