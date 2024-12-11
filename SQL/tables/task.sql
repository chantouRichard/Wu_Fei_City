CREATE TABLE tasks (
    task_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    position VARCHAR(50),
    status VARCHAR(20),
    level VARCHAR(20),
    tags VARCHAR(50), -- 或者使用 ARRAY 类型，取决于数据库支持
    -- poi
    latitude FLOAT,
    longitude FLOAT,
    department VARCHAR(10),
    -- time
    start_time DATETIME NOT NULL,
    deadline DATETIME NOT NULL,
    estimated_min_duration VARCHAR(255) NOT NULL,
    estimated_max_duration VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    -- 任务接取人
    user_accepted_id INTEGER, -- 初始为 NULL

    -- 特化：delete 软删除
    is_deleted BOOLEAN DEFAULT 0,
);


-- others
--ALTER TABLE tasks ADD COLUMN is_deleted BOOLEAN DEFAULT 0;