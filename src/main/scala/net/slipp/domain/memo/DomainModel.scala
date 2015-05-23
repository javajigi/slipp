package net.slipp.domain.memo

import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringStyle, ToStringBuilder}
import org.hibernate.annotations.BatchSize

@BatchSize(size = 20)
class DomainModel {
  override def toString: String = {
    ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
  }

  override def hashCode: Int = {
    HashCodeBuilder.reflectionHashCode(this)
  }

  override def equals(o: scala.Any): Boolean = {
    EqualsBuilder.reflectionEquals(this, o);
  }
}