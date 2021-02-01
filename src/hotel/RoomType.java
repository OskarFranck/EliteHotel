package hotel;

public enum RoomType {

    STANDARD_SINGLE(1, "single", false, true, 2000),
    STANDARD_DOUBLE(2, "single", false, true, 4000),
    LUXURY_SINGLE(1, "queen", true, true, 4000),
    LUXURY_DOUBLE(1, "king", true, true, 6000),
    DELUXE_DOUBLE(1,"king", true, true, 8000);

    private final int numberOfBeds;
    private final String typeOfBed;
    private final boolean hasAC;
    private final boolean breakfastIncluded;
    private final int dailyCharge;

    RoomType(int numberOfBeds, String typeOfBed, boolean hasAC, boolean breakfastIncluded, int dailyCharge) {
        this.numberOfBeds = numberOfBeds;
        this.typeOfBed = typeOfBed;
        this.hasAC = hasAC;
        this.breakfastIncluded = breakfastIncluded;
        this.dailyCharge = dailyCharge;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public String typeOfBed() {
        return typeOfBed;
    }

    public boolean hasAC() {
        return hasAC;
    }

    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public int getDailyCharge() {
        return dailyCharge;
    }

}
