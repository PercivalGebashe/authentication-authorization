-- Email Validation Status
INSERT INTO email_validation_status (email_validation_status_id, status_description)
SELECT 1, 'PENDING'
WHERE NOT EXISTS (
    SELECT 1 FROM email_validation_status WHERE email_validation_status_id = 1
);

INSERT INTO email_validation_status (email_validation_status_id, status_description)
SELECT 2, 'VERIFIED'
WHERE NOT EXISTS (
    SELECT 1 FROM email_validation_status WHERE email_validation_status_id = 2
);

INSERT INTO email_validation_status (email_validation_status_id, status_description)
SELECT 3, 'FAILED'
WHERE NOT EXISTS (
    SELECT 1 FROM email_validation_status WHERE email_validation_status_id = 3
);

-- Roles
INSERT INTO roles (role_id, role_description)
SELECT 1, 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_id = 1
);

INSERT INTO roles (role_id, role_description)
SELECT 2, 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_id = 2
);
