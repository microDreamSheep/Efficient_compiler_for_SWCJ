package test;

import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;

public class main {
    public static void main(String[] args) throws Exception {
        SWCJClassLoader swcjClassLoader = new SWCJClassLoader();
        Class<?> aClass = swcjClassLoader.loadData("a158372551", "E://a.class");
        Object o = aClass.getDeclaredConstructor().newInstance();
    }
}
