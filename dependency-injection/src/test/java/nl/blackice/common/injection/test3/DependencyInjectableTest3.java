package nl.blackice.common.injection.test3;

import nl.blackice.common.injection.Injectable;

@Injectable
public class DependencyInjectableTest3 {
    private final DependencyOfDependencyInjectableTest3 depOfDep;

    public DependencyInjectableTest3(DependencyOfDependencyInjectableTest3 depOfDep) {
        this.depOfDep = depOfDep;
    }

    public int getValue() {
        return 2 * depOfDep.getValue();
    }
}
