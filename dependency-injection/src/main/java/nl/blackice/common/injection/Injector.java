package nl.blackice.common.injection;

import nl.blackice.common.injection.exception.AlreadyOtherInjectableCreated;

import java.util.HashMap;
import java.util.Map;

public class Injector {
    private Injector() {}

    private static final Map<String, Object> injectables = new HashMap<>();

    public static void clear() {
        injectables.clear();
    }

    public static void add(Object injectable) {
        for (Class<?> implementedInterface : getAllImplementedInterfaces(injectable)) {
            addInjectable(implementedInterface.getName(), injectable);
        }
        if (!injectable.getClass().getSuperclass().equals(Object.class)) {
            addInjectable(injectable.getClass().getSuperclass().getName(), injectable);
        }
        addInjectable(injectable.getClass().getName(), injectable);
    }

    private static void addInjectable(String name, Object injectable) {
        checkIfNameAlreadyInUse(name);
        injectables.put(name, injectable);
    }

    public static <T> T get(Class<T> clazz) {
        if (!injectables.containsKey(clazz.getName())) {
            throw injectorNotFoundException(clazz);
        }
        Object injectable = injectables.get(clazz.getName());
        return castInjectable(injectable, clazz);
    }

    public static boolean contains(Class<?> clazz) {
        return injectables.containsKey(clazz.getName());
    }

    private static <T> T castInjectable(Object injectable, Class<T> clazz) {
        if (!isClass(injectable, clazz) && !extendsClass(injectable, clazz) && !implementsClass(injectable, clazz)) {
            throw injectorNotFoundException(clazz);
        }
        return clazz.cast(injectable);
    }

    private static <T> boolean isClass(Object injectable, Class<T> clazz) {
        return injectable.getClass().equals(clazz);
    }

    private static <T> boolean extendsClass(Object injectable, Class<T> clazz) {
        return injectable.getClass().getSuperclass().equals(clazz);
    }

    private static <T> boolean implementsClass(Object injectable, Class<T> clazz) {
        for (Class<?> implementedInterface : getAllImplementedInterfaces(injectable)) {
            if (implementedInterface.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    private static Class<?>[] getAllImplementedInterfaces(Object injectable) {
        return injectable.getClass().getInterfaces();
    }

    private static <T> RuntimeException injectorNotFoundException(Class<T> clazz) {
        return new RuntimeException(String.format("Unknown injectable: %s. Did you add an injectable for it?", clazz.getName()));
    }

    private static void checkIfNameAlreadyInUse(String name) {
        if(injectables.containsKey(name)) {
            throw new AlreadyOtherInjectableCreated(name);
        }
    }
}
