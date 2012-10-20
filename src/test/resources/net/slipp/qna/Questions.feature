Feature: 질문 답변 게시판

Scenario: 태그를 가지는 질문을 등록한다
	Given SLiPP 메인 페이지에서 로그인 후 글쓰기 버튼을 클릭한다.
	When 제목 : SLiPP, 내용 : 지속 가능한 삶, 프로그래밍, 프로그래머, 태그 : java javascript 를 등록한다.
	Then 제목 : SLiPP, 내용 : 지속 가능한 삶, 프로그래밍, 프로그래머, 태그 : java javascript 가 정상적으로 등록되었는지 확인한다.