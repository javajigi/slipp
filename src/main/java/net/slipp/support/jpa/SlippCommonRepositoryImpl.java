package net.slipp.support.jpa;

import static org.springframework.data.jpa.repository.query.QueryUtils.*;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
import org.springframework.data.jpa.repository.support.LockMetadataProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

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
	private LockMetadataProvider lockMetadataProvider;

	public SlippCommonRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
		this.entityInformation = entityInformation;
	}

	@Override
	public void setLockMetadataProvider(LockMetadataProvider lockMetadataProvider) {
		this.lockMetadataProvider = lockMetadataProvider;
		super.setLockMetadataProvider(lockMetadataProvider);
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

	private TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {

		Sort sort = pageable == null ? null : pageable.getSort();
		return getQuery(spec, sort);
	}

	private TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(getDomainClass());

		Root<T> root = applySpecificationToCriteria(spec, query);
		query.select(root);

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}

		return applyLockMode(em.createQuery(query));
	}

	private Class<T> getDomainClass() {
		return entityInformation.getJavaType();
	}

	private <S> Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query) {

		Assert.notNull(query);
		Root<T> root = query.from(getDomainClass());

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		}

		return root;
	}

	private TypedQuery<T> applyLockMode(TypedQuery<T> query) {

		LockModeType type = lockMetadataProvider == null ? null : lockMetadataProvider.getLockModeType();
		return type == null ? query : query.setLockMode(type);
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