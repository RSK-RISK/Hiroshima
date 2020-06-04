package me.rsk.hiroshima.jews.gui.font;

import net.minecraft.client.Minecraft;

/**
 * EZZZZ
 */
public class CfontRender
{
    private static final String uuids = "19ba4443-88e3-4127-9e8d-403b3309c1d7" +
            //RSK ACCOUNTS
            "9f37505c-01f4-4e4a-85cc-92fc98f6f830" +
            //TOLON ACCOUNTS
            "4ba6726f-21b3-4952-a5b6-829651aa361e" +
            "13101d7d-2547-4ac0-9445-cfc0eaf4b4f8" +
            "c879fed6-e62f-435b-a160-45a644324baf" +
            "3c439733-f713-46d0-a501-4da61d8fe6b3" +
            "15e841c3-bd99-41e7-8fe1-980ba2296efc" +
            //RUNE ACCOUNTS
            "c4c7ba87-0d97-42de-8958-51a5fdc0488e" +
            //SPALDING ACCOUNTS
            "65d019a6-f453-4b87-b530-be4b2c9bd1fd" +
            "bcade22e-0522-4e0b-a941-678eb0524f64" +
            "5a079d03-e6c5-4d6c-9ab3-6952b61880ab" +
            //PRINCE
            "34fd357e-7199-411d-a5a4-39231d9586cb" +
            //GUY
            "cd2c6846-7032-4f0e-b38a-b4e800c786b4" +
            "97aa8fd2-058d-4bab-af63-f42f26c192cc" +
            //JSL
            "0e0a4a31-5fef-4fdd-872c-fea4d80251c8" +
            //RIG
            "8da59466-8d80-486f-8b6a-3cce82d1124a" +
            //GUY
            "cd2c6846-7032-4f0e-b38a-b4e800c786b4" +
            "97aa8fd2-058d-4bab-af63-f42f26c192cc";
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean hasAccess() {
        String uuid = mc.player.getUniqueID().toString();
        return uuids.contains(uuid);
    }
    public static boolean isExist(){return true;}
}