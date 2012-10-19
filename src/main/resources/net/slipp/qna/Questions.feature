Feature: 질문 답변 게시판

Scenario: 태그를 가지는 질문을 등록한다
	Given 질문 글쓰기 페이지로 이동한다.
	When java javascript 태그로 질문을 등록한다.
	Then 질문과 태그가 정상적으로 등록되었는지 확인한다.