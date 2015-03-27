package net.slipp.support.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import net.slipp.support.jpa.SlippCommonRepository;
import net.slipp.support.jpa.SlippCommonRepositoryImpl;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class SlippRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		JpaRepositoryFactoryBean<T, S, ID> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new SlippCommonRepositoryFactory(entityManager);
	}

	protected static class SlippCommonRepositoryFactory extends JpaRepositoryFactory {

		public SlippCommonRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
				EntityManager entityManager) {

			Class<?> repositoryInterface = metadata.getRepositoryInterface();

			if (!isSlippCommonRepository(repositoryInterface)) {
				return super.getTargetRepository(metadata, entityManager);
			}

			JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
			SlippCommonRepositoryImpl<?, ?> repo = new SlippCommonRepositoryImpl(entityInformation, entityManager);
			return repo;
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			if (isSlippCommonRepository(metadata.getRepositoryInterface())) {
				return SlippCommonRepositoryImpl.class;
			}
			return super.getRepositoryBaseClass(metadata);
		}

		protected boolean isSlippCommonRepository(Class<?> repositoryInterface) {

			return SlippCommonRepository.class.isAssignableFrom(repositoryInterface);
		}
	}
}
