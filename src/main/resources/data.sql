INSERT INTO menu (
    created_at,
    deleted_at,
    description,
    image_url,
    name,
    price,
    status,
    stock,
    updated_at,
    version
) VALUES
      (
          NOW(),
          NULL,
          '깊고 진한 에스프레소를 물에 희석한 기본 커피',
          'https://picsum.photos/300?1',
          '아메리카노',
          3000,
          'ACTIVE',
          50,
          NOW(),
          1
      ),
      (
          NOW(),
          NULL,
          '부드러운 우유와 진한 에스프레소의 조화',
          'https://picsum.photos/300?2',
          '카페라떼',
          4500,
          'ACTIVE',
          35,
          NOW(),
          1
      ),
      (
          NOW(),
          NULL,
          '달콤한 초콜릿과 에스프레소를 함께 즐기는 커피',
          'https://picsum.photos/300?3',
          '카페모카',
          5000,
          'ACTIVE',
          20,
          NOW(),
          1
      ),
      (
          NOW(),
          NULL,
          '부드러운 바닐라 향이 더해진 라떼',
          'https://picsum.photos/300?4',
          '바닐라라떼',
          5200,
          'ACTIVE',
          12,
          NOW(),
          1
      ),
      (
          NOW(),
          NULL,
          '진하게 추출한 콜드브루',
          'https://picsum.photos/300?5',
          '콜드브루',
          4800,
          'ACTIVE',
          8,
          NOW(),
          1
      ),
      (
          NOW(),
          NULL,
          '진한 에스프레소 샷',
          'https://picsum.photos/300?6',
          '에스프레소',
          2500,
          'INACTIVE',
          15,
          NOW(),
          1
      ),
      (
          NOW(),
          NOW(),
          '시즌 한정 음료',
          'https://picsum.photos/300?7',
          '체리블라썸라떼',
          5900,
          'DELETED',
          0,
          NOW(),
          2
      );

INSERT INTO review (
    content,
    created_at,
    email,
    menu_id,
    menu_name,
    menu_version,
    rating
) VALUES
      (
          '산미가 적고 무난해서 매일 마시기 좋아요.',
          NOW(),
          'abc@abc.com',
          1,
          '아메리카노',
          1,
          5
      ),
      (
          '조금 진한 편이지만 맛있었습니다.',
          NOW(),
          'coffee@naver.com',
          1,
          '아메리카노',
          1,
          4
      ),
      (
          '',
          NOW(),
          'guest@test.com',
          2,
          '카페라떼',
          1,
          5
      ),
      (
          '우유가 부드럽고 고소해서 만족합니다.',
          NOW(),
          'latte@google.com',
          2,
          '카페라떼',
          1,
          5
      ),
      (
          '초콜릿 향이 진해서 디저트랑 잘 어울립니다.',
          NOW(),
          'mocha@test.com',
          3,
          '카페모카',
          1,
          5
      ),
      (
          '생각보다 많이 달아요.',
          NOW(),
          'sweet@abc.com',
          3,
          '카페모카',
          1,
          3
      ),
      (
          '바닐라 향이 은은해서 좋았습니다.',
          NOW(),
          'vanilla@test.com',
          4,
          '바닐라라떼',
          1,
          4
      ),
      (
          '시원하고 깔끔한 맛입니다.',
          NOW(),
          'coldbrew@test.com',
          5,
          '콜드브루',
          1,
          5
      ),
      (
          '조금 연한 느낌이었습니다.',
          NOW(),
          'reviewer@test.com',
          5,
          '콜드브루',
          1,
          3
      );