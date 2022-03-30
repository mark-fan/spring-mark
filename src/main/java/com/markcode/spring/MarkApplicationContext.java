package com.markcode.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luofan
 */
public class MarkApplicationContext {
    private Class classConfig;

    /**
     * 单例池
     */
    private ConcurrentHashMap<String, Object> singletonMap = new ConcurrentHashMap<>();
    /**
     * beandefinitionMap
     */
    private ConcurrentHashMap<String, BeanDefinition> beandefinitionMap = new ConcurrentHashMap<>();


    public MarkApplicationContext(Class springConfig) {
        this.classConfig = springConfig;

        scan(springConfig);

        for (Map.Entry<String, BeanDefinition> entry : beandefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanDefinition);
                singletonMap.put(beanName, bean);
            }
        }


    }

    /**
     * 启动 --> 扫描 --> 路径  --> 类属性  --> beanDefinitionMap
     * @param springConfig
     */
    private void scan(Class springConfig) {
        ComponentScan componentScan = (ComponentScan) springConfig.getDeclaredAnnotation(ComponentScan.class);
        // path
        String value = componentScan.value();
        ClassLoader classLoader = MarkApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(value.replace(".","//"));
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    try {
                        Class<?> aClass = classLoader.loadClass(className.replace("\\", "."));
                        // 假如该类含有component标签 就初始化该类
                        if (aClass.isAnnotationPresent(Component.class)) {
                            Component component = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setaClass(aClass);
                            // 判断是否有作用域，无作用域默认单例
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scope = aClass.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scope.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }
                            beandefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public Object getBean(String beanName) {
        if (beandefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beandefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                return singletonMap.get(beanName);
            } else {
                return createBean(beanDefinition);
            }
        } else {
            return new NullPointerException();
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class aClass = beanDefinition.getaClass();
        try {
            Object instance = aClass.getDeclaredConstructor().newInstance();
            // 依赖注入
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(Autowired.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(instance,getBean(declaredField.getName()));
                }
            }
            return instance;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
