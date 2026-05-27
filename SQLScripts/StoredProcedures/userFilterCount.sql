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