package common.utils;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import common.data.AtmTransaction;


public class ResourceUtilsTests {
    @Test
    void can_read_static_embedded_resources() throws IOException {
        ResourceUtils
            .toStream("/transaction.json", AtmTransaction.class)
            .toList();
    }
}
