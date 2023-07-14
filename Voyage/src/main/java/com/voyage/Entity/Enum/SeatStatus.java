package com.voyage.Entity.Enum;

public enum SeatStatus {
    BOS(1),
    DOLU(2),
    REZERVE(3);

    private final int number;

    SeatStatus(int number) {
        this.number = number;
    }
    public int getSeatStatus() {
        return number;
    }

    public static SeatStatus fromInt(int number) {
        for(SeatStatus type : SeatStatus.values()){
            if (type.getSeatStatus() == number){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SeatStatus: " + number);
    }
}