package net.slipp.support.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

/**
 * Archeage전용 Repository 메소드를 추가한 공통 Repository 구현체
 *
 * @author kwon37xi
 *
 */
@NoRepositoryBean
public class SlippCommonRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
		SlippCommonRepository<T, ID> {

	private EntityManager em;
	private JpaEntityInformation<T, ?> entityInformation;

	public SlippCommonRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
		this.entityInformation = entityInformation;
	}

	@Override
	public T getOne(ID id) {
		T entity = findOne(id);

		if (entity == null) {
			throw new EmptyResultDataAccessException(1);
		}

		return entity;
	}

	@Override
	public T getOne(Specification<T> specs) {
		T entity = findOne(specs);

		if (entity == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return entity;
	}

	@Override
	public T getFirstSortingData(Specification<T> spec, Sort sort){
		return Iterables.getFirst( getAmountSortingData(spec, sort, 1) , null);
	}

	@Override
	public List<T> getAmountSortingData(Specification<T> spec, Sort sort, int amount){
		Pageable pageable = new PageRequest(0, amount, sort);
		return findMore(spec, pageable);
	}

	@Override
	public T persist(T entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public T merge(T entity) {
		return em.merge(entity);
	}

	@Override
	public List<T> findMore(Pageable pageable) {
		return findMore(null, pageable);
	}

	@Override
	public List<T> findMore(Specification<T> spec, Pageable pageable) {
		TypedQuery<T> query = getQuery(spec, pageable);
		return pageable == null ? query.getResultList() : readMorePage(query, pageable, spec);
	}

	private List<T> readMorePage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<T> content = query.getResultList();

		return content;
	}

	@Override
	public T findPrevByNumberId(final Number idValue, Specification<T> spec, final Sort sort) {
		Preconditions.checkArgument(spec instanceof Specifications, "spec은 Specifications 객체여야 함.");

		Specifications<T> specifications = (Specifications<T>) spec;

		final SingularAttribute<? super T, ?> idAttribute = getIdAttribute();

		String idName = idAttribute.getName();

		final Order order = getIdOrder(sort, idName);

		Specification<T> idCondition = new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = null;

				@SuppressWarnings("unchecked")
				Path<? extends Number> path = (Path<? extends Number>) root.get(idAttribute);

				if (order.getDirection() == Direction.DESC) {

					predicate = cb.gt(path, idValue);
				} else {
					predicate = cb.lt(path, idValue);
				}
				return predicate;
			}

		};

		Specifications<T> finalSpec = getSpecificationsWithIdCondition(specifications, idCondition);

		Direction direction = order.getDirection() == Direction.DESC ? Direction.ASC : Direction.DESC;

		return findNextorPrevResultByNumberId(order, finalSpec, direction);
	}

	private SingularAttribute<? super T, ?> getIdAttribute() {
		final SingularAttribute<? super T, ?> idAttribute = entityInformation.getIdAttribute();

		if (!Number.class.isAssignableFrom(entityInformation.getIdType())) {
			throw new IllegalArgumentException("ID가 숫자형일 경우에만 사용할 수 있습니다.");
		}
		return idAttribute;
	}

	private Order getIdOrder(final Sort sort, String idName) {
		final Order order = sort.getOrderFor(idName);

		if (order == null) {
			throw new IllegalArgumentException("ID 프라퍼티(" + idName + ")의 정렬기준이 sort에 존재해야 합니다.");
		}
		return order;
	}

	private Specifications<T> getSpecificationsWithIdCondition(Specifications<T> specifications,
			Specification<T> idCondition) {
		Specifications<T> finalSpec = null;

		if (specifications == null) {
			finalSpec = Specifications.where(idCondition);
		} else {
			finalSpec = specifications.and(idCondition);
		}
		return finalSpec;
	}

	private T findNextorPrevResultByNumberId(final Order keyOrder, Specifications<T> finalSpec, Direction direction) {
		Pageable pageable = new PageRequest(0, 1, new Sort(keyOrder.with(direction)));
		List<T> results = findMore(finalSpec, pageable);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public T findNextByNumberId(final Number idValue, Specification<T> spec, Sort sort) {
		Preconditions.checkArgument(spec instanceof Specifications, "spec은 Specifications 객체여야 함.");

		Specifications<T> specifications = (Specifications<T>) spec;

		final SingularAttribute<? super T, ?> idAttribute = getIdAttribute();

		String idName = idAttribute.getName();

		final Order order = getIdOrder(sort, idName);

		Specification<T> idCondition = new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = null;

				@SuppressWarnings("unchecked")
				Path<? extends Number> path = (Path<? extends Number>) root.get(idAttribute);

				if (order.getDirection() == Direction.DESC) {

					predicate = cb.lt(path, idValue);
				} else {
					predicate = cb.gt(path, idValue);
				}
				return predicate;
			}

		};

		Specifications<T> finalSpec = getSpecificationsWithIdCondition(specifications, idCondition);

		return findNextorPrevResultByNumberId(order, finalSpec, order.getDirection());
	}
}