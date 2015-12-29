package net.slipp.support.jpa

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import javax.persistence.EntityManager
import java.io.Serializable

protected class SlippCommonRepositoryFactory(entityManager: EntityManager)
  extends JpaRepositoryFactory(entityManager) {
  protected override def getRepositoryBaseClass(metadata: RepositoryMetadata): Class[_] = {
    if (isSlippCommonRepository(metadata.getRepositoryInterface)) {
      return classOf[SlippCommonRepositoryImpl[_, _ <: Serializable]]
    }
    return super.getRepositoryBaseClass(metadata)
  }

  protected def isSlippCommonRepository(repositoryInterface: Class[_]): Boolean = {
    return classOf[SlippCommonRepository[_, _ <: Serializable]].isAssignableFrom(repositoryInterface)
  }
}

class SlippRepositoryFactoryBean[T <: Repository[S, ID], S, ID <: Serializable] extends JpaRepositoryFactoryBean[T, S, ID] {
  protected override def createRepositoryFactory(entityManager: EntityManager): RepositoryFactorySupport = {
    return new SlippCommonRepositoryFactory(entityManager)
  }
}
