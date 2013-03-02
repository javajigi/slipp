package net.slipp.web.qna;

import javax.annotation.Resource;

import net.slipp.service.qna.MigrationService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/migrations")
public class MigrationsController {
	@Resource(name = "migrationService")
	private MigrationService migrationService;

	
	@RequestMapping("/latestparticipant")
	public String updateDateAndLatestParticipant() {
		migrationService.updateDateAndLatestParticipant();
		return "redirect:/";
	}
}
