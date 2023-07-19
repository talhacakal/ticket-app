package com.ticket.Annotation;

public enum Role {
    ROLE_DEV,
    ROLE_ADMIN,
    ROLE_INSTITUTIONAL,
    ROLE_INDIVIDUAL;

//    public final int authorityLevel;
//    Role(int authorityLevel) {
//        this.authorityLevel = authorityLevel;
//    }
//    public static Role fromAuthorityLevel(int value) {
//        for(Role type : Role.values()){
//            if (type.authorityLevel == value){
//                return type;
//            }
//        }
//        throw new IllegalArgumentException("Invalid Role: " + value);
//    }
//
//    public static int getAuthorityLevel(String role){
//        for (Role type : Role.values()){
//            if (type.toString().equals(role)) return type.authorityLevel;
//        }
//        throw new IllegalArgumentException("Invalid Role " + role);
//    }
}
