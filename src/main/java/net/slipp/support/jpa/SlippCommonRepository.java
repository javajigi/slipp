package net.slipp.support.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.social.ResourceNotFoundException;

/**
 * SLiPP전용 Repository 메소드를 추가한 공통 Repository Interface
 *
 * 전체 페이지 개수 카운트가 불필요한 More 방식 페이징 메소드를 기본 제공하고 있다.
 *
 * @param <T>
 * @param <ID>
 */
public interface SlippCommonRepository<T, ID extends Serializable> extends JpaRepository<T, ID>,
		JpaSpecificationExecutor<T> {

	/**
	 * 엔티티를 ID기준으로 하나 검색한다. 단, 엔티티가 존재하지 않을 경우 {@link ResourceNotFoundException
	 * ResourceNotFoundException}을 발생시킨다.
	 *
	 * @param id
	 * @return
	 */
	T getOne(ID id);

	/**
	 * 엔티티를 하나 검색한다. 단, 엔티티가 존재하지 않을 경우 {@link ResourceNotFoundException
	 * ResourceNotFoundException}을 발생시킨다.
	 *
	 * @param specs
	 * @return
	 */
	T getOne(Specification<T> specs);

	/**
	 * 정렬한 데이터에서 amount 값 만큼 데이타를 가지고 온다.
	 * @param specs
	 * @param sort
	 * @return
	 */
	List<T> getAmountSortingData(Specification<T> spec, Sort sort, int amount);

	T getFirstSortingData(Specification<T> spec, Sort sort);

	/**
	 * 엔티티를 즉시 저장한다. 존재 여부 검사등은 하지 않는다.
	 *
	 * @param entity
	 * @return
	 */
	T persist(T entity);

	/**
	 * 엔티티 수정. Persistence Context에 추가.
	 *
	 * @param entity
	 * @return
	 */
	T merge(T entity);

	/**
	 * 전체 데이터 개수를 세지 않는 More 방식 페이징 쿼리 메소드
	 *
	 * @param pageable
	 * @return
	 */
	List<T> findMore(Pageable pageable);

	/**
	 * 전체 데이터 개수를 세지 않는 More 방식 페이징 쿼리 메소드
	 *
	 * @param spec
	 *            조건
	 * @param pageable
	 * @return
	 */
	List<T> findMore(Specification<T> spec, Pageable pageable);

	/**
	 * 기준 엔티티보다 이전(정렬순서상 위) 엔티티를 찾는다.
	 *
	 * 제약사항 : 정렬기준이 항상 숫자로 된 ID 하나 일 때만 가능하다.
	 *
	 * @param idValue
	 *            기준 엔티티의 ID 프라퍼티 값. 숫자.
	 * @param spec
	 *            검색 조건
	 * @param sort
	 *            기본 목록의 정렬 기준. 항상 이 정렬 기준에 id가 들어있어야 한다.
	 * @return
	 */
	T findPrevByNumberId(Number idValue, Specification<T> spec, Sort sort);

	/**
	 * 기준 엔티티보다 다음(정렬 순서상 아래) 엔티티를 찾는다.
	 *
	 * 제약사항 : 정렬기준이 항상 숫자로 된 ID 하나 일 때만 가능하다.
	 *
	 * @param idValue
	 *            기준 엔티티의 ID 프라퍼티 값. 숫자.
	 * @param spec
	 *            검색 조건
	 * @param sort
	 *            기본 목록의 정렬 기준. 항상 이 정렬 기준에 id가 들어있어야 한다.
	 * @return
	 */
	T findNextByNumberId(Number idValue, Specification<T> spec, Sort sort);
}
