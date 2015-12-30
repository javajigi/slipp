package net.slipp.support.jpa

import org.hibernate.dialect.MySQL5InnoDBDialect

class Mysql5BitBooleanDialect extends MySQL5InnoDBDialect {
  registerColumnType(java.sql.Types.BOOLEAN, "bit")
}
