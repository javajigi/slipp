package net.slipp.domain.tag;

public enum TaggedType {
    TAGGED, DETAGGED;
    public static final String COLUMN_DEFINITION = "enum('TAGGED','DETAGGED')";
}
