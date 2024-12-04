SELECT *
FROM tasks
WHERE status = 'WAITING'
    AND (6371 * acos(
        cos(radians(39.9042)) * cos(radians(latitude)) *
        cos(radians(longitude) - radians(116.4074)) +
        sin(radians(39.9042)) * sin(radians(latitude))
    )) <= 500
    AND NOW() < deadline;
