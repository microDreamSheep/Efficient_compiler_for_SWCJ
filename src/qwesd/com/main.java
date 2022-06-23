package qwesd.com;

import com.midream.sheep.swcj.core.build.builds.effecient.EffecientBuilder;
import com.midream.sheep.swcj.core.build.builds.effecient.pojo.SWCJCodeClass;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.parse.bystr.BetterXmlParseTool;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import qwesd.pojo;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;


public class main {
    public static void main(String[] args) throws Exception {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory();
        swcjXmlFactory.setBuilder(new EffecientBuilder());
        swcjXmlFactory.setParseTool(new BetterXmlParseTool());
        long start = System.currentTimeMillis();
        swcjXmlFactory.parse(new File("E:\\SWCJ\\core\\SWCJ\\target\\test-classes\\Efficient.xml"));
        pojo html = (pojo)swcjXmlFactory.getWebSpiderById("getHtml");
        long end = System.currentTimeMillis();
        System.out.println("分析加构建类花费"+(end-start));
        String[] images = html.gethtml("5");
        for (String image : images) {
            System.out.println(image);
        }
    }
}
