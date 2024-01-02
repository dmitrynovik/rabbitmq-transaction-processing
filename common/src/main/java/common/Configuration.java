package common;

// Naming this "Configuration" in a Spring Boot based application may be a bit confusing. Wdyt about using an interface
// with a default method to represent the constant but still have a more code like feel? Something like the following:
/*
interface TransactionsExchange {
    default String getName() { return "txExchange"; }
}
*/


public class Configuration {

    public class TransactionsExchange {
        public static String getName() { return "txExchange"; }
    }
}
