package com;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.effecient.EffecientBuilder;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import org.xml.sax.SAXException;
import test.image;
import test.pojo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class main {
    public static void main(String[] args) throws IOException, ConfigException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InterfaceIllegal {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory();
        swcjXmlFactory.setBuilder(new EffecientBuilder()).parse(new File("E:\\SWCJ\\core\\SWCJ\\target\\test-classes\\test.xml"));
        pojo html = (pojo)swcjXmlFactory.getWebSpider("getHtml");
        image[] images = html.get("5", "4");
        for (image image : images) {
            System.out.println(image.toString());
        }
    }
}
