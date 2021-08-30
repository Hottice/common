package nl.blackice.common.injection.test1;

import nl.blackice.common.injection.Injectable;

@Injectable
public class NoArgsInjectable {
    public int getValue() {
        return 42;
    }
}
