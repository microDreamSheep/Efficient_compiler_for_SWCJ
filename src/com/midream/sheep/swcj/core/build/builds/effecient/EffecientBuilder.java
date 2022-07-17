package com.midream.sheep.swcj.core.build.builds.effecient;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.core.build.builds.effecient.data.EConstant;
import com.midream.sheep.swcj.core.build.builds.effecient.data.EVariables;
import com.midream.sheep.swcj.core.build.builds.effecient.data.CoreTable;
import com.midream.sheep.swcj.core.build.builds.effecient.function.ByteTool;
import com.midream.sheep.swcj.core.build.builds.effecient.pojo.SWCJCodeClass;
import com.midream.sheep.swcj.core.build.builds.effecient.pojo.VariableCode;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilderAbstract;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.util.function.StringUtil;


import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class EffecientBuilder extends SWCJBuilderAbstract {
    @Override
    protected Object buildObject(RootReptile rr, ReptileConfig rc) {
        //构建阶段---->获取核心信息
        SWCJCodeClass swcjCodeClass = new SWCJCodeClass();
        CoreTable econtrol = swcjCodeClass.getCoreTable();
        SWCJClass sclass;
        try {
            sclass = BuildTool.getSWCJClass(rr,rc);
        } catch (ConfigException | EmptyMatchMethodException e) {
            throw new RuntimeException(e);
        }
        //注入阶段--->注入全局信息
        injectData(sclass,econtrol);
        //注入方法/增加变量表
        injectMethods(swcjCodeClass,sclass,swcjCodeClass.getCount(),econtrol,rr,rc);
        try {
            return swcjcl.loadData(sclass.getClassName(), getWholeClass(swcjCodeClass)).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private void injectData(SWCJClass sclass,CoreTable coreTable){
        //注入接口
        byte[] interbytes = sclass.getItIterface().replace(".", "/").getBytes();
        coreTable.Constants.set(26, new VariableCode(new byte[]{0x01, 0x00, (byte) interbytes.length}, interbytes));
        //注入类名
        byte[] classNameBytes = sclass.getClassName().replace(".", "/").getBytes();
        coreTable.Constants.set(25, new VariableCode(new byte[]{0x01, 0x00, (byte) classNameBytes.length}, classNameBytes));

    }
    private void injectMethods(SWCJCodeClass swcjCodeClass,SWCJClass swcjClass,EVariables eVariables,CoreTable econtrol,RootReptile rr,ReptileConfig rc){
        int count = 0;//指针
        for (Map.Entry<String, SWCJMethod> entry : swcjClass.getMethods().entrySet()) {
            SWCJMethod value = entry.getValue();
            //方法计数器自增
            eVariables.methods_count[1]++;
            List<String> injection = new LinkedList<>();
            BuildTool.getMethodParametric(rr.getRu().get(count),value,injection);
            List<String> valueVars = value.getVars();
            if (count == 0) {
                //第一次方法拼接：
                first(value,econtrol,rr,rc,injection,valueVars);
                count++;
                continue;
            }
            injectionMethod(swcjCodeClass,value,count,rr,rc,injection,valueVars);
            //常量计数加8
            eVariables.constant_Count[1] += 8;
            count++;
        }
    }
    private void injectionMethod(SWCJCodeClass swcjCodeClass,SWCJMethod value,int porint,RootReptile rr,ReptileConfig rc,List<String> injection,List<String> vars1){
        //准备数据
        CoreTable coreTable = swcjCodeClass.getCoreTable();
        //准备常量--->返回数组
        byte[] returnArrayBytes = ("[L" + value.getReturnType().replace("[]", "") + ";").replace(".", "/").getBytes();
        coreTable.Constants.add(new VariableCode(new byte[]{0x01, 0x00, (byte) returnArrayBytes.length}, returnArrayBytes));
        coreTable.Constants.add(new VariableCode(new byte[]{0x07},new byte[]{0x00, (byte) coreTable.Constants.size()}));
        int returnArrayPointer = coreTable.Constants.size();
        //准备常量--->返回实例
        byte[] returnBytes = (value.getReturnType().replace("[]", "")).replace(".", "/").getBytes();
        coreTable.Constants.add(new VariableCode(new byte[]{0x01, 0x00, (byte) returnBytes.length}, returnBytes));
        coreTable.Constants.add(new VariableCode(new byte[]{0x07},new byte[]{0x00, (byte) coreTable.Constants.size()}));
        int returnPointer = coreTable.Constants.size();
        //注入方法名
        byte[] methodName = value.getMethodName().getBytes();
        coreTable.Constants.add(new VariableCode(new byte[]{0x01, 0x00, (byte) methodName.length}, methodName));
        int methodNamePointer = coreTable.Constants.size();
        //注入方法描述
        byte[] desBytes = ByteTool.getMethodDescription(value.getVars().size(), ("[L" + value.getReturnType().replace("[]", "") + ";").replace(".", "/"));
        coreTable.Constants.add(new VariableCode(new byte[]{0x01, 0x00, (byte) desBytes.length}, desBytes));
        int descriptionPointer = coreTable.Constants.size();
        //注入执行逻辑
        String executeCharacter = StringUtil.getExecuteCharacter(rr.getRu().get(porint), injection, rc, rr, value).replace("\\\"", "\"");
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            shortBuf[i] = (byte) (((short) executeCharacter.getBytes().length >>> ((shortBuf.length - 1 - i) * 8)) & 0xff);
        }
        coreTable.Constants.add(new VariableCode(ByteTool.ConcatenatedArray(new byte[]{0x01}, shortBuf), executeCharacter.getBytes()));
        coreTable.Constants.add(new VariableCode(new byte[]{0x08},new byte[]{0x00, (byte) coreTable.Constants.size()}));
        coreTable.methods.add(EConstant.getInstanceMethod(methodNamePointer, descriptionPointer, returnPointer, returnArrayPointer, coreTable.Constants.size(),vars1.size()));
    }
    private void first(SWCJMethod value,CoreTable coreTable,RootReptile rr,ReptileConfig rc,List<String> injection,List<String> vars1){
        //拼接返回值
        byte[] returnArrayBytes = ("[L" + value.getReturnType().replace("[]", "") + ";").replace(".", "/").getBytes();
        coreTable.Constants.set(24, new VariableCode(new byte[]{0x01, 0x00, (byte) returnArrayBytes.length}, returnArrayBytes));
        byte[] returnBytes = (value.getReturnType().replace("[]", "")).replace(".", "/").getBytes();
        coreTable.Constants.set(21, new VariableCode(new byte[]{0x01, 0x00, (byte) returnBytes.length}, returnBytes));
        //注入方法名
        byte[] methodName = value.getMethodName().getBytes();
        coreTable.Constants.set(14, new VariableCode(new byte[]{0x01, 0x00, (byte) methodName.length}, methodName));
        //方法描述符拼接
        byte[] desBytes = ByteTool.getMethodDescription(value.getVars().size(), ("[L" + value.getReturnType().replace("[]", "") + ";").replace(".", "/"));
        coreTable.Constants.set(15, new VariableCode(new byte[]{0x01, 0x00, (byte) desBytes.length}, desBytes));
        //方法执行逻辑
        byte[] bytes = StringUtil.getExecuteCharacter(rr.getRu().get(0), injection, rc, rr, value).replace("\\\"", "\"").getBytes();
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            shortBuf[i] = (byte) (((short) bytes.length >>> ((shortBuf.length - 1 - i) * 8)) & 0xff);
        }
        coreTable.Constants.set(18, new VariableCode(ByteTool.ConcatenatedArray(new byte[]{0x01}, shortBuf),bytes));
        coreTable.methods.add(SWCJCodeClass.init_method);
        coreTable.methods.add( EConstant.getInstanceMethod(0x0F, 0x10, 7, 9,0x04, vars1.size()));
    }

    public byte[] getWholeClass(SWCJCodeClass swcjCodeClass) {
        CoreTable econtrol = swcjCodeClass.getCoreTable();
        EVariables eVariables = swcjCodeClass.getCount();
        int size = 24;
        for (VariableCode code : econtrol.Constants) {
            size+=code.getCodes().length+code.getDescription().length;
        }
        for (byte[] bytes : econtrol.methods) {
            size += bytes.length;
        }
        byte[] datas = new byte[size+2];
        int zz = 0;
        zz = zz + copy(SWCJCodeClass.magic, datas, zz);
        zz += copy(SWCJCodeClass.versionNumber, datas, zz);
        zz += copy(eVariables.constant_Count, datas, zz);
        for (VariableCode code : econtrol.Constants) {
            zz+=copy(code.getDescription(),datas,zz);
            zz+=copy(code.getCodes(),datas,zz);
        }
        zz += copy(SWCJCodeClass.access_flags_class, datas, zz);
        zz += copy(SWCJCodeClass.this_super_interfaces_class, datas, zz);
        //类拼接
        zz += copy(SWCJCodeClass.filed_info, datas, zz);
        zz += copy(eVariables.methods_count, datas, zz);
        for (byte[] bytes : econtrol.methods) {
            zz += copy(bytes, datas, zz);
        }
        copy(new byte[]{0x00,0x00}, datas, zz);
        return datas;
    }

    public int copy(byte[] source, byte[] to, int zz) {
        System.arraycopy(source, 0, to, zz, source.length);
        return source.length;
    }

}
