create table petition
(
    id               int auto_increment
        primary key,
    create_time      datetime      null,
    update_time      datetime      null,
    is_deleted       int           null,
    version          int           null,
    type             int           null,
    is_handled       int default 0 null,
    content          mediumtext    null,
    proposer_account int           null,
    handler_account  int           null,
    start_date       datetime      null,
    end_date         datetime      null,
    reply            mediumtext    null
);

INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-27 22:07:40', '2022-03-27 22:07:43', 0, 1, 1, 1, '家里有事', 666666, 666665, '2022-03-27 22:08:19', '2022-03-27 22:08:21', '1已批准');
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-23 18:47:18', '2022-03-27 12:41:27', 0, 8, 1, 1, '家里有事', 666666, 888943, '2022-03-24 18:47:11', '2022-03-25 21:25:48', '1No problem');
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-28 01:53:34', '2022-03-28 01:53:36', 0, 2, 0, 1, '家里有事', 666666, 888943, '2022-03-28 01:53:57', '2022-03-28 01:53:59', '0不准通过');
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-27 18:58:06', '2022-03-27 18:58:06', 0, 2, 0, 1, '家里有事', 888909, 666666, '2022-03-21 22:00:00', '2022-03-28 21:59:59', '0请开具证明');
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-28 08:57:49', '2022-03-28 08:57:49', 0, 2, 0, 1, '家里有事', 888920, 666666, '2022-03-27 11:00:00', '2022-03-31 10:59:59', '1情况属实，同意');
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-28 09:22:51', '2022-03-28 09:22:51', 0, 1, 0, 0, '家里有事', 888925, null, '2022-03-28 11:00:00', '2022-03-30 10:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-27 20:29:44', '2022-03-27 20:29:44', 0, 2, 1, 0, '家里有事', 888929, null, '2022-03-20 22:00:00', '2022-03-22 21:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-03-28 09:30:17', '2022-03-28 09:30:17', 0, 1, 1, 0, '家里有事', 888938, null, '2022-03-27 11:00:00', '2022-03-29 10:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-04 22:02:07', '2022-04-04 22:02:07', 0, 1, 1, 0, '家里有事，需请假1天。', 666665, null, '2022-04-04 11:00:00', '2022-04-06 10:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-04 22:14:53', '2022-04-04 22:14:53', 0, 1, 0, 0, '因家里有事，需请假。', 666665, null, '2022-04-11 11:00:00', '2022-04-15 10:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-04 22:30:44', '2022-04-04 22:30:44', 0, 1, 0, 0, '因家里有急事，需回家', 666665, null, '2022-04-04 11:00:00', '2022-04-07 10:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-04 22:40:35', '2022-04-04 22:40:35', 0, 1, 1, 0, '家里有事，需回家', 666665, null, '2022-04-12 11:00:00', '2022-04-14 10:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-05 03:44:10', '2022-04-05 03:44:10', 0, 1, 1, 0, '122112', 666665, null, '2022-04-13 16:00:00', '2022-04-15 15:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-05 03:46:57', '2022-04-05 03:46:57', 0, 1, 1, 0, '12122121', 666665, null, '2022-04-27 16:00:00', '2022-04-29 15:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-05 11:49:11', '2022-04-05 11:49:11', 0, 1, 1, 0, '家里有事，需回家', 666665, null, '2022-04-05 00:00:00', '2022-04-06 23:59:59', null);
INSERT INTO application.petition (create_time, update_time, is_deleted, version, type, is_handled, content, proposer_account, handler_account, start_date, end_date, reply) VALUES ('2022-04-05 11:55:29', '2022-04-05 11:55:29', 0, 2, 1, 1, '有急事，需请假', 666665, 666666, '2022-04-13 00:00:00', '2022-04-14 23:59:59', '3已通过');