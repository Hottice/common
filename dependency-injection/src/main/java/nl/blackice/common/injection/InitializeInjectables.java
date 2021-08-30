package nl.blackice.common.injection;

import nl.blackice.common.injection.exception.DependencyException;
import nl.blackice.common.injection.exception.InjectableShouldHaveOneConstructorException;
import nl.blackice.common.injection.exception.UnableToCreateInjectableException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class InitializeInjectables {
    private InitializeInjectables() {
    }

    public static void run(String path) {
        Reflections reflections = createReflectionForPath(path);
        Set<Class<?>> injectables = reflections.getTypesAnnotatedWith(Injectable.class);
        List<Class<?>> todoInjectables = new LinkedList<>(injectables);
        List<Class<?>> skippedInjectables = new LinkedList<>();
        while (!todoInjectables.isEmpty()) {
            skippedInjectables.clear();
            for (Class<?> injectable : todoInjectables) {
                if (!shouldCreate(injectable)) {
                    continue;
                }
                if (!canCreateInstance(injectable)) {
                    skippedInjectables.add(injectable);
                    continue;
                }
                createInstance(injectable);
            }
            checkForErrors(todoInjectables, skippedInjectables);
            todoInjectables = new LinkedList<>(skippedInjectables);
        }
    }

    private static void checkForErrors(List<Class<?>> todoInjectables, List<Class<?>> skippedInjectables) {
        if (todoInjectables.size() == skippedInjectables.size()) {
            throw new DependencyException(String.format("Unable to create injectables %s",
                    todoInjectables.stream()
                            .map(Class::getName)
                            .reduce((a, b) -> a + ", " + b).orElse("")),
                    new DependencyException(String.format("Missing implementations for %s",
                            skippedInjectables.stream()
                                    .flatMap((clazz) -> getParametersThatCanNotBeResolved(clazz).stream())
                                    .map(Class::getName)
                                    .reduce((a, b) -> a + ", " + b).orElse(""))));
        }
    }

    private static Reflections createReflectionForPath(String path) {
        return new Reflections(path);
    }

    private static boolean shouldCreate(Class<?> injectable) {
        return !Injector.contains(injectable);
    }

    private static boolean canCreateInstance(Class<?> injectable) {
        if (injectable.getConstructors().length != 1) {
            throw new InjectableShouldHaveOneConstructorException(String.format("%s: An injectable should only have exactly one constructor!", injectable.getName()));
        }
        return allParametersAreInjectable(injectable);
    }

    private static boolean allParametersAreInjectable(Class<?> injectable) {
        Constructor<?> constructor = injectable.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            if (!Injector.contains(parameterType)) {
                return false;
            }
        }
        return true;
    }

    private static List<Class<?>> getParametersThatCanNotBeResolved(Class<?> injectable) {
        List<Class<?>> unresolvableParameters = new ArrayList<>();
        Constructor<?> constructor = injectable.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            if (!Injector.contains(parameterType)) {
                unresolvableParameters.add(parameterType);
            }
        }
        return unresolvableParameters;
    }

    private static void createInstance(Class<?> injectable) {
        Constructor<?> constructor = injectable.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        try {
            if (parameterTypes.length == 0) {
                Injector.add(constructor.newInstance());
                return;
            }
            Injector.add(constructor.newInstance(getInstanceParams(parameterTypes)));
        } catch (Exception exc) {
            throw new UnableToCreateInjectableException("Unable to create injectable", exc);
        }
    }

    private static Object[] getInstanceParams(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int idx = 0; idx < parameterTypes.length; idx++) {
            parameters[idx] = Injector.get(parameterTypes[idx]);
        }
        return parameters;
    }
}
