package net.slipp.domain;

public enum ProviderType {
	facebook,
	twitter,
	google,
	slipp;
	
	public static final String COLUMN_DEFINITION = "enum ('facebook', 'twitter', 'google', 'slipp')";
}
