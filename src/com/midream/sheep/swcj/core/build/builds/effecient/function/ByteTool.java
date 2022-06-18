package com.midream.sheep.swcj.core.build.builds.effecient.function;

public class ByteTool {
    public static byte[] ConcatenatedArray(byte[] one,byte[] two){
        byte[] result=new byte[one.length+two.length];
        System.arraycopy(one, 0, result, 0, one.length);
        System.arraycopy(two, 0, result, one.length, two.length);
        return result;
    }
    public static byte[] getMethodDescription(int count,String returnType){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0;i<count;i++){
            sb.append("Ljava/lang/String;");
        }
        sb.append(")");
        sb.append(returnType);
        return sb.toString().getBytes();
    }
}
