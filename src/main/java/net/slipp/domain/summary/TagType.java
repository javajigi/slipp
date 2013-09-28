package net.slipp.domain.summary;


	
public enum TagType {
	meta("content", "property", "og:image"),
	link("href", "type", "image/x-icon");
	
	private String attrName;
	private String attrResource;
	private String attrResourceValue;
	
	TagType(String attrName, String attrResource, String attrResourceValue){
		this.attrName = attrName;
		this.attrResource = attrResource;
		this.attrResourceValue = attrResourceValue;
	}
	
	public boolean matchResource(String v){
		return getAttrResourceValue().equals(v);
	}
	
	public String getTag(){
		return this.name();
	}
	
	public String getAttrName() {
		return attrName;
	}

	public String getAttrResource() {
		return attrResource;
	}

	public String getAttrResourceValue() {
		return attrResourceValue;
	}

}
