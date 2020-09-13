package com.wym.init;


class  SuperClass{

    static {

        System.out.println("父类初始化");
    }

    public static int value = 123;
}

class SubClass extends SuperClass {

    static {
        System.out.println("子类初始化");
    }

}
//public class NotInitialization {
//
//    /**
//     * 对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过子类引用父类中定义的静态变量，只会触发父类的初始化而不会触发子类的初始化
//     * @param args
//     */
//    public static void main(String[] args) {
//        System.out.println(SubClass.value);
//    }
//}
public class NotInitialization {


    /**
     * jvm会为数组类型创造一个继承自Object的子类，数组的属性和防范都实现在这个类中，这个类包装了数组的访问
     * @param args
     */
    public static void main(String[] args) {
        SubClass[] arr = new SubClass[10];
        System.out.println(arr.getClass());  //class [Lcom.wym.init.SubClass
    }
}