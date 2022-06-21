package com.midream.sheep.swcj.core.build.builds.effecient.pojo;

public class VariableCode {
    private byte[] description;
    private byte[] codes;

    public VariableCode(byte[] description, byte[] codes) {
        this.description = description;
        this.codes = codes;
    }

    public VariableCode(byte[] codes) {
        this.codes = codes;
        description = new byte[0];
    }

    public byte[] getDescription() {
        return description;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

    public byte[] getCodes() {
        return codes;
    }

    public void setCodes(byte[] codes) {
        this.codes = codes;
    }
}
