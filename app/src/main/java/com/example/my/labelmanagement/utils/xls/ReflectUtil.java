package com.example.my.labelmanagement.utils.xls;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReflectUtil<T> {


    /**
     * 反射调用指定构造方法创建对象
     *
     * @param clazz    对象类型
     * @param argTypes 参数类型
     * @param args     构造参数
     * @return 返回构造后的对象
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public  T invokeConstructor(Class clazz, Class<?>[] argTypes,
                                      Object[] args) throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Constructor constructor = clazz.getConstructor(argTypes);
        return (T) constructor.newInstance(args);
    }

    /**
     * 反射调用指定对象属性的getter方法
     *
     * @param
     * @param target    指定对象
     * @param fieldName 属性名
     * @return 返回调用后的值
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public Object invokeGetter(T target, String fieldName)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchFieldException {
// 如果属性名为xxx,则方法名为getXxx
        String methodName = "get" + StringUtil.firstCharUpperCase(fieldName);
        Method method = target.getClass().getMethod(methodName);
        return method.invoke(target);
    }

    /**
     * 反射调用指定对象属性的setter方法
     *
     * @param
     * @param target    指定对象
     * @param fieldName 属性名
     *                  参数类型
     * @param args      参数列表
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void invokeSetter(T target, String fieldName, Object args)
            throws NoSuchFieldException, SecurityException,
            NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // 如果属性名为xxx,则方法名为setXxx
        String methodName = "set" + StringUtil.firstCharUpperCase(fieldName);
        Class<?> clazz = target.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        Method method = clazz.getMethod(methodName, field.getType());
        method.invoke(target, args);
    }


    public static void main(String[] args) {
        System.out.println("=====test=====");

//        try {
//            Class clazz = User.class;
//            T user = ReflectUtil.invokeConstructor(clazz,
//                    new Class<?>[]{long.class, String.class, int.class,
//                            String.class, double.class}, new Object[]{1001,
//                            "Linux", 30,"123", 20.55});
//            System.out.println(user);
//            ReflectUtil.invokeSetter(user,"salery", 2055);
//            System.out.println(user);
//            Object ret = ReflectUtil.invokeGetter(user,"salery");
//            System.out.println(ret);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


//    public void test() {
//        System.out.println("===test====");
//
//        try {
//            Class clazz = User.class;
//            ReflectUtil userUtils = new ReflectUtil<User>();
//            User user = (User) userUtils.invokeConstructor(clazz,
//                    new Class<?>[]{String.class,
//                            String.class, String.class, int.class}, new Object[]{"xu",
//                            "1", "shangdi", 22});
//            System.out.println(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
