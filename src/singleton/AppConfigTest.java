package singleton;

public class AppConfigTest {
    public static void main(String[] args) {
        AppConfig first = AppConfig.getInstance();
        AppConfig second = AppConfig.getInstance();

        if (first != second) {
            throw new AssertionError("Les deux appels doivent retourner la meme instance.");
        }
        if (!"EUR".equals(first.getCurrency())) {
            throw new AssertionError("La devise initiale doit etre EUR.");
        }

        System.out.println("AppConfigTest : OK (meme instance, devise EUR)");
    }
}