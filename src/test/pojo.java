package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface pojo {
    @WebSpider("getHtml")
    image[] get(String in,String ins);
}
