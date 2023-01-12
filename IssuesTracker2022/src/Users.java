
public class Users {
private String password ;
private String occupation;
public Users() {
	
}

public Users(String password, String occupation) {
	this.password = password;
	this.occupation= occupation;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getOccupation() {
	return occupation;
}

public void setOccupation(String occupation) {
	this.occupation = occupation;
}

@Override
public String toString() {
	return "Users [password=" + password + ", occupation=" + occupation + "]";
}
	
	
}
