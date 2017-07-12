package net.slipp.web.qna

import javax.annotation.Resource

import net.slipp.service.qna.MigrationService
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

@RestController
@RequestMapping(Array("/migrations"))
class MigrationsController(@Resource(name = "migrationService") migrationService: MigrationService) {
  
  @RequestMapping(Array(""))
  def index() = {
    migrationService.migrateFacebookPostId
    "migration success!"
  }
  
  def this() = this(null)
}