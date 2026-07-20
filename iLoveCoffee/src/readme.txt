Pending -> Confirm  오후 2시에 하고

1시 50분에 주문 2시에 처리
2시 5분에 확인 -> Pending
재시도 3회(1분간격) 해보고 -> 관리자 화면에서 처리로 넘긴다

준비하는데 1분이 걸린다
Confirm -> 1분 후 prepare로 전환

1분 후에 외부API 붙는다고 가정
-> dispatch
1분 후에 -> deliver 호출