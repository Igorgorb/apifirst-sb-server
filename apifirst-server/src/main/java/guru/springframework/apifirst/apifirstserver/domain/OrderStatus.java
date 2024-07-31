package guru.springframework.apifirst.apifirstserver.domain;

public enum OrderStatus {
    NEW("NEW"),

    HOLD("HOLD"),

    SHIPPED("SHIPPED"),

    DELIVERED("DELIVERED"),

    CLOSED("CLOSED");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
