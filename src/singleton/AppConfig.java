package singleton;

public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final String currency = "EUR";

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getCurrency() {
        return currency;
    }
}