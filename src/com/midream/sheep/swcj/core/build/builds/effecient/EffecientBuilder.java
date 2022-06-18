package com.midream.sheep.swcj.core.build.builds.effecient;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.effecient.data.EConstant;
import com.midream.sheep.swcj.core.build.builds.effecient.data.EVariables;
import com.midream.sheep.swcj.core.build.builds.effecient.data.Econtrol;
import com.midream.sheep.swcj.core.build.builds.effecient.function.ByteTool;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilderAbstract;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.util.function.StringUtil;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class EffecientBuilder extends SWCJBuilderAbstract {
    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        Econtrol econtrol = new Econtrol();
        SWCJClass sclass = null;
        try {
            sclass = BuildTool.getSWCJClass(rr);
            //注入接口
            byte[] interbytes = sclass.getItIterface().replace(".","/").getBytes();
            econtrol.Constants.set(26, ByteTool.ConcatenatedArray(new byte[]{0x01,0x00, (byte) interbytes.length}, interbytes));
            //注入类名
            byte[] classNameBytes = sclass.getClassName().replace(".","/").getBytes();
            econtrol.Constants.set(25,ByteTool.ConcatenatedArray(new byte[]{0x01,0x00,(byte)classNameBytes.length }, classNameBytes));
            //注入方法
            Set<Map.Entry<String, SWCJMethod>> set = sclass.getMethods().entrySet();
            int count = 0;//指针
            for (Map.Entry<String, SWCJMethod> entry : set) {
                SWCJMethod value = entry.getValue();
                //方法计数器自增
                EVariables.methods_count[1]++;

                ReptileUrl ru = rr.getRu().get(count);
                StringBuilder vars = new StringBuilder();
                List<String> vars1 = value.getVars();
                String[] split2 = ru.getInPutName().split(",");
                List<String> injection = new LinkedList();
                int len = split2.length;
                if (ru.getInPutName().trim().equals("")) {
                    len = 0;
                }

                if (len > vars1.size()) {
                    try {
                        throw new InterfaceIllegal("方法参数不统一");
                    } catch (InterfaceIllegal var14) {
                        var14.printStackTrace();
                    }
                } else {
                    int i;
                    if (len == vars1.size() && len != 0) {
                        for(i = 0; i < split2.length; ++i) {
                            StringUtil.add(vars, vars1.get(i), " ", split2[i], ",");
                            injection.add(split2[i]);
                        }
                    } else {
                        System.err.println("SWCJ:警告：你的接口有部分参数没有用到,方法:" + ru.getName());

                        for(i = 0; i < len; ++i) {
                            StringUtil.add(vars, vars1.get(i), " ", split2[i], ",");
                            injection.add(split2[i]);
                        }

                        for(i = len; i < vars1.size(); ++i) {
                            StringUtil.add(vars, vars1.get(i), " args", i, ",");
                        }
                    }
                }

                String varString = "";
                if (vars.length() != 0) {
                    varString = vars.substring(0, vars.lastIndexOf(","));
                }

                if(count==0){
                    //拼接返回值
                    byte[] returnArrayBytes =  ("[L"+value.getReturnType().replace("[]","")+";").replace(".","/").getBytes();
                    econtrol.Constants.set(24,ByteTool.ConcatenatedArray(new byte[]{0x01,0x00,(byte)returnArrayBytes.length }, returnArrayBytes));

                    byte[] returnBytes =  (value.getReturnType().replace("[]","")).replace(".","/").getBytes();
                    econtrol.Constants.set(21,ByteTool.ConcatenatedArray(new byte[]{0x01,0x00,(byte)returnBytes.length }, returnBytes));
                    //注入方法名
                    byte[] methodName = value.getMethodName().getBytes();
                    econtrol.Constants.set(14,ByteTool.ConcatenatedArray(new byte[]{0x01,0x00,(byte)methodName.length }, methodName));
                    //方法描述符拼接
                    byte[] desBytes = ByteTool.getMethodDescription(value.getVars().size(), ("[L" + value.getReturnType().replace("[]", "") + ";").replace(".", "/"));
                    econtrol.Constants.set(15,ByteTool.ConcatenatedArray(new byte[]{0x01,0x00,(byte)desBytes.length }, desBytes));
                    //方法执行逻辑
                    String executeCharacter = StringUtil.getExecuteCharacter(rr.getRu().get(count), injection, rc, rr, value).replace("\\\"","\"");
                    byte[] bytes = executeCharacter.getBytes();
                    byte[] shortBuf = new byte[2];
                    for(int i=0;i<2;i++) {
                        int offset = (shortBuf.length - 1 -i)*8;
                        shortBuf[i] = (byte)(((short) bytes.length>>>offset)&0xff);
                    }
                    econtrol.Constants.set(18,ByteTool.ConcatenatedArray(ByteTool.ConcatenatedArray(new byte[]{0x01},shortBuf),executeCharacter.getBytes()));
                    continue;
                }
                //常量计数加8
                EVariables.constant_Count[1]+=8;
                count++;
            }
            try {
                OutputStream os = new FileOutputStream("E://a.class");
                os.write(EConstant.magic);
                os.write(EConstant.versionNumber);
                os.write(EVariables.constant_Count);
                for (byte[] bytes : econtrol.Constants) {
                    os.write(bytes);
                }
                os.write(EConstant.access_flags_class);
                os.write(EConstant.this_super_interfaces_class);
                os.write(EConstant.filed_info);
                os.write(EVariables.methods_count);

                os.write(EConstant.init_method);
                os.write(new byte[]{
                        0x00, 0x01, 0x00, 0x0F, 0x00, 0x10, 0x00, 0x01, 0x00, 0x0E, 0x00, 0x00, 0x00, 0x31, 0x00, 0x06,
                        0x00, 0x03, 0x00, 0x00, 0x00, 0x25, (byte) 0xBB, 0x00, 0x02, 0x59, (byte) 0xB7, 0x00, 0x03, 0x12, 0x04, 0x05,
                        (byte) 0xBD, 0x00, 0x05, 0x59, 0x03, 0x2B, 0x53, 0x59, 0x04, 0x2C, 0x53, (byte) 0xB6, 0x00, 0x06, 0x03, (byte) 0xBD,
                        0x00, 0x07, (byte) 0xB9, 0x00, 0x08, 0x02, 0x00, (byte) 0xC0, 0x00, 0x09, (byte) 0xB0, 0x00, 0x00, 0x00, 0x00, 0x00,
                        0x00
                });
                os.flush();
                os.close();
                System.out.println("输出完毕");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }
        return null;
    }
}
