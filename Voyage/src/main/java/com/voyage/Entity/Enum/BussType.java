package com.voyage.Entity.Enum;

public enum BussType {
    TEK("2+1"),
    CIFT("2+2"),
    UCAK("0");

    private final String bussType;

    private BussType(String description) {
        this.bussType = description;
    }

    public String getBussType() {
        return bussType;
    }

    public static BussType fromString(String bussType) {
        for(BussType type : BussType.values()){
            if (type.getBussType().equalsIgnoreCase(bussType)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BussType: " + bussType);
    }
}
