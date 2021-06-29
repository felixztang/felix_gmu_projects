package client;

public class User {

	private String name;
	private String ip;
	
	public User(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIP() {
		return ip;	
	}
	
	public boolean equals(Object u) {
		if(!(u instanceof User)) {
			return false;
		}
		
		return name.equals(((User)u).name);
	}
}
