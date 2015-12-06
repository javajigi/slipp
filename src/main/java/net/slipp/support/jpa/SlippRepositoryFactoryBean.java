package net.slipp.support.jpa;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

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
