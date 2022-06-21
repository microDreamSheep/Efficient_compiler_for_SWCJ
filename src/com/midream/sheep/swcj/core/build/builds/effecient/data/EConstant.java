package com.midream.sheep.swcj.core.build.builds.effecient.data;

import com.midream.sheep.swcj.core.build.builds.effecient.function.ByteTool;
import com.sun.org.apache.bcel.internal.generic.BIPUSH;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class EConstant {
    //实例方法模板0xff都是注入值的地方
    /*4--->方法名注入
     *
     * */
    public static final byte[] InstanceMethod_Template_before = {
            /*22*/
            0x00, 0x01,//访问标识
            0x00, (byte) 0xFF, //方法名
            0x00, (byte) 0xFF,//方法描述
            0x00, 0x01,//表数量
            0x00, 0x0E,//code表
            0x00, 0x00, 0x00, 0x35,//表长度
            0x00, 0x06,//栈最大深度
            0x00, 0x10,//max_local
            0x00, 0x00, 0x00, 0x29,//code_length
            /*13*/
            (byte) 0xBB, 0x00, 0x02,//new
            0x59,//压栈
            (byte) 0xB7, 0x00, 0x03,//调用分析器构造方法
            0x12, (byte) 0xFF,//idc 从常量池拿执行逻辑
            0x10, (byte) 0xFF,
            (byte) 0xBD, 0x00, 0x05,//创建object引用类型数组
            /*留给字符注入*/
    };
    private static final byte[] InstanceMethod_Template_after = {
            (byte) 0xB6, 0x00, 0x06,//调用实例方法
            0x03,//将0推至栈顶
            (byte) 0xBD, 0x00, (byte) 0xFF,//创建返回值数组 ff:实例类型
            (byte) 0xB9, 0x00, 0x08, 0x02,
            0x00, (byte) 0xC0, 0x00, (byte) 0xFF,//ff:数组类型
            (byte) 0xB0,

            0x00, 0x00,//没有异常表
            0x00, 0x00//其他表数量为2
    };
    private static byte[] BipushCode = {0x59, 0x10, (byte) 0xFF, 0x19, (byte) 0xFF, 0x53};
    public static byte[] getBipushCode(int count){
        int len = 0;
        byte[] Codes = new byte[count*6];
        for(int i = 0;i<count;i++){
            byte[] bytes = cloneByteCode();
            bytes[2] = (byte) i;
            bytes[4] = (byte) (i+1);
            for (byte b : bytes) {
                Codes[len] = b;
                len++;
            }
        }
        return Codes;
    }
    private static byte[] cloneByteCode(){
        return BipushCode.clone();
    }
    public static byte[] getInstanceMethod(int methodname,int methoddescription,int returnClass,int returnClasssz,int executePointer,int count){
        int point = 0;
        byte[] datas = new byte[InstanceMethod_Template_before.length+InstanceMethod_Template_after.length+count*6];
        for (byte b : InstanceMethod_Template_before) {
            datas[point] = b;
            point++;
        }
        //注入方法
        //注入方法名
        datas[3] = (byte) methodname;
        //注入方法描述
        datas[5] = (byte) methoddescription;
        datas[17] = (byte) ((byte) 1+count);
        //注入方法长度
        datas[13] = (byte) (InstanceMethod_Template_after.length+InstanceMethod_Template_before.length-14+count*6);
        datas[21] = (byte) (35+count*6-5);
        //注入方法执行逻辑引用
        datas[30] = (byte) executePointer;
        datas[32] = (byte) count;
        byte[] code = getBipushCode(count);
        //注入实际执行
        for (byte b : code) {
            datas[point] = b;
            point++;
        }
        byte[] clone = InstanceMethod_Template_after.clone();
        clone[6] = (byte) returnClass;
        clone[14] = (byte) returnClasssz;
        for (byte b : clone) {
            datas[point] = b;
            point++;
        }
        return datas;
    }
}
