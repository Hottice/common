package nl.blackice.common.injection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InjectorTest {
    private static class InjectableTestClassWithoutInterfaces {};
    private static class InjectableTestClassWithInterface implements InjectableTestInterface {}
    private interface InjectableTestInterface {}

    @BeforeEach
    public void clearInjectors() {
        Injector.clear();
    }

    @Test
    public void testInjectorAddingClassWithoutInterfaces() {
        InjectableTestClassWithoutInterfaces injectable = new InjectableTestClassWithoutInterfaces();
        Injector.add(injectable);
        Assertions.assertEquals(Injector.get(InjectableTestClassWithoutInterfaces.class), injectable);
    }

    @Test
    public void testInjectorAddingClassWithInterfaces() {
        InjectableTestClassWithInterface injectable = new InjectableTestClassWithInterface();
        Injector.add(injectable);
        Assertions.assertEquals(Injector.get(InjectableTestClassWithInterface.class), injectable);
        Assertions.assertEquals(Injector.get(InjectableTestInterface.class), injectable);
    }

    @Test
    public void testErrorWhenNoInjectorFound() {
        Assertions.assertThrows(RuntimeException.class, () -> Injector.get(InjectableTestClassWithInterface.class));
    }
}

