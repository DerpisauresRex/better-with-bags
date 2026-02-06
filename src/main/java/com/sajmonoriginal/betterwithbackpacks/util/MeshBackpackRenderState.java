package com.sajmonoriginal.betterwithbackpacks.util;

public class MeshBackpackRenderState {
    private static boolean hasMeshBackpack = false;

    public static void setHasMeshBackpack(boolean value) {
        hasMeshBackpack = value;
    }

    public static boolean hasMeshBackpack() {
        return hasMeshBackpack;
    }
}
