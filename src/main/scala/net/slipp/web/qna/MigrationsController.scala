package net.slipp.web.qna

import javax.annotation.Resource

import net.slipp.service.qna.MigrationService

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(Array("/migrations"))
class MigrationsController(@Resource(name = "migrationService") migrationService: MigrationService) {
  
  @RequestMapping(Array(""))
  def index() = "redirect:/"
  
  def this() = this(null)
}