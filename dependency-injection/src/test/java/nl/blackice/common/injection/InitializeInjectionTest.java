package nl.blackice.common.injection;

import nl.blackice.common.injection.test1.NoArgsInjectable;
import nl.blackice.common.injection.test2.DependencyInjectableTest2;
import nl.blackice.common.injection.test2.SingleDependencyInjectableTest2;
import nl.blackice.common.injection.test3.DependencyInjectableTest3;
import nl.blackice.common.injection.test3.DependencyOfDependencyInjectableTest3;
import nl.blackice.common.injection.test3.SingleDependencyInjectableTest3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InitializeInjectionTest {

    @BeforeEach
    public void clearInjectors() {
        Injector.clear();
    }

    @Test
    public void testPathFilter() {
        InitializeInjectables.run("nl.blackice.common.injection.test1");
        Assertions.assertFalse(Injector.contains(SingleDependencyInjectableTest2.class));
        Assertions.assertFalse(Injector.contains(SingleDependencyInjectableTest3.class));
    }

    @Test
    public void testNoConstructorArgs() {
        InitializeInjectables.run("nl.blackice.common.injection.test1");
        Assertions.assertTrue(Injector.contains(NoArgsInjectable.class));
        Assertions.assertEquals(42, Injector.get(NoArgsInjectable.class).getValue());
    }

    @Test
    public void testSimpleDependencies() {
        InitializeInjectables.run("nl.blackice.common.injection.test2");
        Assertions.assertTrue(Injector.contains(DependencyInjectableTest2.class));
        Assertions.assertTrue(Injector.contains(SingleDependencyInjectableTest2.class));
        Assertions.assertEquals(40, Injector.get(SingleDependencyInjectableTest2.class).getValue());
    }

    @Test
    public void testExtendedDependencies() {
        InitializeInjectables.run("nl.blackice.common.injection.test3");
        Assertions.assertTrue(Injector.contains(DependencyOfDependencyInjectableTest3.class));
        Assertions.assertTrue(Injector.contains(DependencyInjectableTest3.class));
        Assertions.assertTrue(Injector.contains(SingleDependencyInjectableTest3.class));
        Assertions.assertEquals(200, Injector.get(SingleDependencyInjectableTest3.class).getValue());
    }
}
