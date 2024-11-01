package iesb.pg2024.project.app;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.*;

public class DependencyNavigator {

    private static final Set<Class<?>> visitedClasses = new HashSet<>();
    private static final List<String> dependencyPaths = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String className = "java.lang.Object"; // Classe que você deseja mapear
            Class<?> clazz = Class.forName(className);
            mapDependencies(clazz, "");
            printDependencies();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void mapDependencies(Class<?> clazz, String path) {
        if (visitedClasses.contains(clazz)) {
            return;
        }
        
        visitedClasses.add(clazz);
        String currentPath = path.isEmpty() ? clazz.getSimpleName() : path + " -> " + clazz.getSimpleName();

        // Adiciona a classe atual ao caminho de dependências
        dependencyPaths.add(currentPath);

        // Obter os métodos da classe
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Class<?>[] paramTypes = method.getParameterTypes();
            for (Class<?> paramType : paramTypes) {
                mapDependencies(paramType, currentPath); // Mapear dependências dos parâmetros
            }
            Class<?> returnType = method.getReturnType();
            mapDependencies(returnType, currentPath); // Mapear dependências do tipo de retorno
        }

        // Obter os supertipos da classe
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            mapDependencies(superClass, currentPath); // Mapear dependências da superclasse
        }

        // Obter interfaces implementadas
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> intf : interfaces) {
            mapDependencies(intf, currentPath); // Mapear dependências das interfaces
        }
    }

    private static void printDependencies() {
        System.out.println("Dependências mapeadas:");
        for (String path : dependencyPaths) {
            System.out.println(path);
        }
    }
}
