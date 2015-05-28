package net.slipp.support.jpa

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.hibernate.annotations.BatchSize

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