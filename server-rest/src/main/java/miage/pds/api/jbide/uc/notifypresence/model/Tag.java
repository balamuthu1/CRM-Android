package miage.pds.api.jbide.uc.notifypresence.model;


public class Tag {
	
	  private String id;
	  private String location;
   
	  public Tag() {
	  }
	   
	  public Tag(String idUser) {
	    setId(idUser);
	   
	  }
	   
	  public String getId() {
	    return id;
	  }
	   
	  public void setId(String id) {
	    this.id = id;
	  }
	  
	  public String toString() {
	    return "[" + getId() + "]";
	  }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	}