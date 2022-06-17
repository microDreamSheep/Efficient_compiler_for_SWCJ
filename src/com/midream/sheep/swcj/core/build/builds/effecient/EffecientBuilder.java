package com.midream.sheep.swcj.core.build.builds.effecient;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.effecient.data.EConstant;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilderAbstract;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

import java.util.Map;
import java.util.Objects;

public class EffecientBuilder extends SWCJBuilderAbstract {
    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        SWCJClass sclass = null;
        try {
            sclass = BuildTool.getSWCJClass(rr);
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }
        return null;
    }
}
