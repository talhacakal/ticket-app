package com.voyage.Entity.Enum;

public enum GenderType {
    EMPTY(0),
    ERKEK(1),
    KADIN(2);

    private final int value;
    GenderType(int value) {
        this.value = value;
    }
    public int getGenderValue() {
        return value;
    }
    public static GenderType fromInt(int value) {
        for(GenderType type : GenderType.values()){
            if (type.getGenderValue() == value){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid GenderType: " + value);
    }
}
