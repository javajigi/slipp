package controllers;

import play.mvc.With;
import supports.web.Check;
import supports.web.GAESecure;
import supports.web.Role;
import models.Tag;
import controllers.CRUD.For;

@For(Tag.class)
@With(GAESecure.class)
@Check(Role.ROLE_ADMIN_USER)
public class TagAdmins extends CRUD {

}
