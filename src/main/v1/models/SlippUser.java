package models;

import play.data.validation.Email;
import play.data.validation.Required;
import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.embed.EmbeddedMap;

@EmbeddedMap
public class SlippUser {
	private String email;
	private String nickName;
	
	public SlippUser() {}

	public SlippUser(String email, String nickName) {
		this.email = email;
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public String getNickName() {
		return nickName;
	}

	@Override
	public String toString() {
		return "SlippUser [email=" + email + ", nickName=" + nickName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SlippUser other = (SlippUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
}
