package com.midream.sheep.swcj.core.build.builds.effecient.pojo;

import com.midream.sheep.swcj.core.build.builds.effecient.data.CoreTable;
import com.midream.sheep.swcj.core.build.builds.effecient.data.EVariables;

public class SWCJCodeClass {
    //java魔数
    public static final byte[] magic = {(byte) 0xCA, (byte) 0XFE, (byte) 0xBA, (byte) 0xBE};
    //1.8版本号
    public static final byte[] versionNumber = {0x00, 0x00, 0x00, 0x34};
    //类标识符
    public static final byte[] access_flags_class = {0x00, 0x21};
    //类名，父类:Object，接口
    public static final byte[] this_super_interfaces_class = {
            0x00, 0x0A, 0x00, 0x05, 0x00, 0x01, 0x00, 0x0B
    };
    //字段表
    public static final byte[] filed_info = {0x00, 0x00};
    //构造方法
    public static final byte[] init_method = {
            0x00, 0x01, 0x00, 0x0C, 0x00, 0x0D,//方法信息
            0x00, 0x01,//属性表数量
            0x00, 0x0E,//code表
            0x00, 0x00, 0x00, 0x11,//表长度
            0x00, 0x01,//栈最大深度
            0x00, 0x01,//max_local
            0x00, 0x00, 0x00, 0x05,//code_length
            0x2A, (byte) 0xB7, 0x00, 0x01, (byte) 0xB1,
            0x00, 0x00, 0x00, 0x00,//code属性表计数
    };
    private final CoreTable coreTable = new CoreTable();
    private final EVariables count = new EVariables();

    public EVariables getCount() {
        return count;
    }

    public CoreTable getCoreTable() {
        return coreTable;
    }
}
