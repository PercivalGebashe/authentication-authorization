INSERT INTO email_validation_status (email_validation_status_id, status_description)
VALUES (1, 'PENDING')
ON CONFLICT DO NOTHING;

INSERT INTO email_validation_status (email_validation_status_id, status_description)
VALUES (2, 'VERIFIED')
ON CONFLICT DO NOTHING;

INSERT INTO email_validation_status (email_validation_status_id, status_description)
VALUES (3, 'FAILED')
ON CONFLICT DO NOTHING;
