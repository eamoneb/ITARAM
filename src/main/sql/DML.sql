INSERT INTO incident VALUES (
    1,
    'foo'
);
COMMIT;
INSERT INTO incident VALUES (
    2,
    'bar'
);
COMMIT;
SELECT * FROM incident;