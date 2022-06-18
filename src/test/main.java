package test;

import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;

public class main {
    public static void main(String[] args) throws Exception {
        SWCJClassLoader swcjClassLoader = new SWCJClassLoader();
        Class<?> data = swcjClassLoader.loadData("a6c5a503991aa4a7097f10ffcd49d7992", "E://a.class");
        Object o = data.getDeclaredConstructor().newInstance();
        pojo a =  (pojo)o;
        //please use Latin characters Offsets:
        System.out.println("开始调用");
        image[] it = a.getIt("5", "5");
        System.out.println("调用完毕");

        for (image image : it) {
            System.out.println(image);
        }
    }
}
