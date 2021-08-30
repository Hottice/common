package nl.blackice.common.injection.test2;

import nl.blackice.common.injection.Injectable;

@Injectable
public class SingleDependencyInjectableTest2 {
    private final DependencyInjectableTest2 dep;

    public SingleDependencyInjectableTest2(DependencyInjectableTest2 dep) {
        this.dep = dep;
    }

    public int getValue() {
        return dep.getValue() * 2;
    }
}
