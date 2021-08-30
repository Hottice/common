package nl.blackice.common.injection.test3;

import nl.blackice.common.injection.Injectable;

@Injectable
public class SingleDependencyInjectableTest3 {
    private final DependencyInjectableTest3 dep;
    private final DependencyOfDependencyInjectableTest3 depOfDep;

    public SingleDependencyInjectableTest3(DependencyInjectableTest3 dep, DependencyOfDependencyInjectableTest3 depOfDep) {
        this.dep = dep;
        this.depOfDep = depOfDep;
    }

    public int getValue() {
        return dep.getValue() * depOfDep.getValue();
    }
}
