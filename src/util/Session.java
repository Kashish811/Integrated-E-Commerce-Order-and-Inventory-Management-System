package util;

/**
 * Class Session
 * Simple comment for Session
 */
public class Session {
    private static int customerId;

        public static void setCustomerId(int id) {
        customerId = id;
    }

        public static int getCustomerId() {
        return customerId;
    }
}
