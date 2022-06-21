package test.com;

import com.midream.sheep.swcj.core.build.builds.effecient.EffecientBuilder;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import test.image;
import test.pojo;

import java.io.File;


public class main {
    public static void main(String[] args) throws Exception {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory();
        swcjXmlFactory.setBuilder(new EffecientBuilder());
            swcjXmlFactory.parse(new File("E:\\SWCJ\\core\\SWCJ\\target\\test-classes\\test.xml"));
        long start = System.currentTimeMillis();
        pojo html = (pojo)swcjXmlFactory.getWebSpider("getHtml");
        long end = System.currentTimeMillis();
        System.out.println("构建类花费"+(end-start));
        image[] images = html.gethtml("5");
        for (image image : images) {
            System.out.println(image.toString());
        }
    }
}
