package net.slipp.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.stereotype.Controller

@Controller
class RobotsController {
    @RequestMapping(Array("/robots.txt"))
    def getRobots = "robotsAllowed"
}