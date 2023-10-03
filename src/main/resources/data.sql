SELECT *
FROM howbachu.topic;
INSERT INTO howbachu.topic (date, title, sub_a, sub_b, a, b)
VALUES (CURRENT_DATE(), '월 250 백수 vs 월 800 직장인', '월 250 백수', '월 800 직장인', 6, 4);

INSERT INTO member (avatar, email, is_deleted, mbti, password, status_message, username)
VALUES ('avatar1.jpg', 'user1@example.com', 0, 'INTJ', 'password1', 'INTJ 스타일로 세상을 탐험.', '탐험가1'),
       ('avatar2.jpg', 'user2@example.com', 0, 'ENTP', 'password2', 'ENTP의 자부심!', '사색가2'),
       ('avatar3.jpg', 'user3@example.com', 0, 'INFP', 'password3', 'INFP 컬러로 꿈꾸며 살기.', '꿈꾸는사람3'),
       ('avatar4.jpg', 'user4@example.com', 0, 'ESTJ', 'password4', 'ESTJ 방식으로 리드하기.', '리더4'),
       ('avatar5.jpg', 'user5@example.com', 0, 'ISTP', 'password5', 'ISTP의 모험을 즐기는 중.', '모험가5'),
       ('avatar6.jpg', 'user6@example.com', 0, 'ENFP', 'password6', '세상은 무한한 가능성으로 가득 차 있어.', '비전가6'),
       ('avatar7.jpg', 'user7@example.com', 0, 'ISFJ', 'password7', '늘 다른 사람들을 생각하는 중.', '돌봄전문가7'),
       ('avatar8.jpg', 'user8@example.com', 0, 'ESFP', 'password8', 'ESFP의 반짝임을 세상에 전하며.', '반짝이는사람8'),
       ('avatar9.jpg', 'user9@example.com', 0, 'INFJ', 'password9', 'INFJ로서 세상을 더 나은 곳으로 만들기.', '변화를만드는사람9'),
       ('avatar10.jpg', 'user10@example.com', 0, 'ENTJ', 'password10', 'ENTJ의 정신으로 세상을 정복하며.', '정복자10');


INSERT INTO vote (created_at, modified_at, select_sub_title, selection, member_id, topic_id)
VALUES (NOW(), NOW(), '월 250 백수', 'A', 1, 1),
       (NOW(), NOW(), '월 250 백수', 'A', 2, 1),
       (NOW(), NOW(), '월 800 직장인', 'B', 3, 1),
       (NOW(), NOW(), '월 250 백수', 'A', 4, 1),
       (NOW(), NOW(), '월 800 직장인', 'B', 5, 1),
       (NOW(), NOW(), '월 250 백수', 'A', 6, 1),
       (NOW(), NOW(), '월 800 직장인', 'B', 7, 1),
       (NOW(), NOW(), '월 250 백수', 'A', 8, 1),
       (NOW(), NOW(), '월 250 백수', 'A', 9, 1),
       (NOW(), NOW(), '월 800 직장인', 'B', 10, 1);


INSERT INTO opin (content, like_cnt, parent_id, vote_id, created_at, modified_at)
VALUES ('월 250 백수가 좋아요!', 5, NULL, 1, NOW(), NOW()),
       ('월 800 직장인이 더 좋아요!', 3, NULL, 2, NOW(), NOW()),
       ('백수의 자유를 선택합니다.', 2, NULL, 3, NOW(), NOW()),
       ('직장인이 더 안정적이라 생각해요.', 7, NULL, 4, NOW(), NOW()),
       ('직장인의 일상이 더 바쁜 것 같아요.', 6, NULL, 5, NOW(), NOW()),
       ('백수의 여유로움이 좋습니다.', 4, NULL, 6, NOW(), NOW()),
       ('직장인으로서 더 많은 경험을 얻을 수 있어요.', 1, NULL, 7, NOW(), NOW()),
       ('백수의 자유로운 시간이 부럽습니다.', 3, NULL, 8, NOW(), NOW()),
       ('직장인의 삶이 더 풍요롭다고 생각해요.', 5, NULL, 9, NOW(), NOW()),
       ('백수와 직장인, 둘 다 장단점이 있어요.', 8, NULL, 10, NOW(), NOW());


INSERT INTO opin (content, like_cnt, parent_id, vote_id, created_at, modified_at)
VALUES ('백수의 자유로움에 동감합니다.', 2, 1, 1, NOW(), NOW()),
       ('그런데 경제적 안정이 걱정되네요.', 1, 1, 1, NOW(), NOW()),
       ('직장인의 안정감이 좋죠.', 3, 2, 2, NOW(), NOW()),
       ('하지만 스트레스도 많아요.', 2, 2, 2, NOW(), NOW()),
       ('백수의 시간은 정말 소중해요.', 2, 3, 3, NOW(), NOW()),
       ('그렇지만 돈은 어떻게 벌까요?', 2, 3, 3, NOW(), NOW()),
       ('직장인은 경험도 많이 쌓아요.', 4, 4, 4, NOW(), NOW()),
       ('그런데 여가 시간은 부족하죠.', 1, 4, 4, NOW(), NOW()),
       ('직장인이라도 일과 시간을 잘 분배하면 좋아요.', 3, 5, 5, NOW(), NOW()),
       ('그렇지만 야근이 많으면 힘들죠.', 2, 5, 5, NOW(), NOW()),
       ('백수의 생활도 괜찮아 보여요.', 3, 6, 6, NOW(), NOW()),
       ('하지만 계속 그렇게 할 수 있을까요?', 1, 6, 6, NOW(), NOW()),
       ('직장인의 삶은 성취감이 있어요.', 4, 7, 7, NOW(), NOW()),
       ('그렇지만 휴식도 필요하죠.', 3, 7, 7, NOW(), NOW()),
       ('백수로 지내면서 취미 생활을 즐기는 것도 좋아요.', 4, 8, 8, NOW(), NOW()),
       ('그렇지만 나중에 후회할까요?', 2, 8, 8, NOW(), NOW()),
       ('직장인이면서도 워라밸을 중요시하는 것이 중요해요.', 5, 9, 9, NOW(), NOW()),
       ('그렇지만 회사 문화에 따라 다르겠죠.', 2, 9, 9, NOW(), NOW()),
       ('저도 그렇게 생각해요. 장단점이 뚜렷하죠.', 4, 10, 10, NOW(), NOW()),
       ('결국 선택은 개인의 가치관에 달려있어요.', 3, 10, 10, NOW(), NOW());


INSERT INTO howbachu.topic (date, title, sub_a, sub_b, a, b)
VALUES
    (DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY ), '봄이 좋냐? 가을이 좋냐?', '봄', '가을', 35, 55),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 2 DAY ), '여름이 좋냐? 겨울이 좋냐?', '여름', '겨울', 42, 33),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 3 DAY ), '둘 중 하나만 키워야 한다면 고양이? 강아지?', '고양이', '강아지', 10, 53),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 4 DAY ), '둘 중 하나만 먹어야 한다면 짜장면? 짬뽕?', '짜장면', '짬뽕', 34, 66),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 5 DAY ), '분식집에서 둘 중 하나가 사라진다면 떡볶이? 순대?', '떡볶이', '순대', 34, 23),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 6 DAY ), '여름에 물놀이는 바다 vs 계곡', '바다', '계곡', 13, 26),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY ), '애인과 낭만있는 데이트 글램핑 vs 호텔', '글램핑', '호텔', 64, 43),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 8 DAY ), '치킨은 역시 후라이드 vs 양념', '후라이드', '양념', 78, 35),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 9 DAY ), '평생 둘 중 하나만 먹어야 한다면 소주 vs 맥주', '소주', '맥주', 67, 37),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY ), '둘 중 하나가 영원히 사라진다면 라면? 삼겹살?', '라면', '삼겹살', 45, 64),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 11 DAY ), '애인과 데이트를 한다면 연극? 영화?', '연극', '영화', 75, 12),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 12 DAY ), '둘 중 하나가 사라진다면, 로켓배송 vs 음식배달', '로켓배송', '음식배달', 34, 65),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 13 DAY ), '엄마가 갑자기 국수를 해주신다, 잔치국수 vs 비빔국수', '잔치국수', '비빔국수', 45, 74),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 14 DAY ), '친구 팬티에 내 손? 아니면 내 팬티에 친구 손?', '친구 팬티에 내 손', '내 팬티에 친구 손', 93, 12),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 15 DAY ), '김밥은 역시 꼭다리? 몸통?', '꼭다리', '몸통', 34, 67),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 16 DAY ), '붕어빵은 팥붕? 슈붕?', '팥붕', '슈붕', 56, 74),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 17 DAY ), '붕어빵은 꼬리부터 머리부터', '꼬리부터', '머리부터', 44, 45);


INSERT INTO report (created_at, modified_at, content, reason, type, reported_id, reporter_id)
VALUES
    (DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY), '스팸성 광고글입니다.', '스팸 내용', 'SPAM', 1, 2),
    (DATE_ADD(CURRENT_DATE(), INTERVAL -2 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -2 DAY), '해당 글에는 욕설이 포함되어 있습니다.', '욕설 및 혐오 발언', 'AVERSION', 2, 3),
    (DATE_ADD(CURRENT_DATE(), INTERVAL -3 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -3 DAY), '게시글에 부적절한 이미지가 포함되어 있습니다.', '부적절한 내용', 'INAPPROPRIATE_CONTENT', 3, 4),
    (DATE_ADD(CURRENT_DATE(), INTERVAL -4 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -4 DAY), '프로필 사진이 부적절합니다.', '부적절한 프로필', 'INAPPROPRIATE_PROFILE', 4, 5),
    (DATE_ADD(CURRENT_DATE(), INTERVAL -5 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -5 DAY), '개인 전화번호가 노출되었습니다.', '개인정보 유출', 'PRIVACY_LEAK', 5, 1),
    (DATE_ADD(CURRENT_DATE(), INTERVAL 0 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 0 DAY), '게시글에 허위의 사실이 포함되어 있습니다.', '허위정보', 'FALSEHOOD', 1, 3),
    (DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY), '해당 게시글은 타인의 저작권을 침해하고 있습니다.', '저작권 침해', 'COPYRIGHT', 2, 4),
    (DATE_ADD(CURRENT_DATE(), INTERVAL -3 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL -3 DAY), '커뮤니티 규정에 명시되지 않은 문제가 있습니다.', '기타', 'ETC', 3, 5);



