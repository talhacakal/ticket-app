package com.voyage.Entity.Enum;

public enum VehicleType {
    OTOBUS("Otobüs"),
    UCAK("Uçak");

    private final String vehicleType;

    private VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public static VehicleType fromString(String vehicleType) {
        for(VehicleType type : VehicleType.values()){
            if (type.getVehicleType().equalsIgnoreCase(vehicleType)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid VehicleType: " + vehicleType);
    }
}
