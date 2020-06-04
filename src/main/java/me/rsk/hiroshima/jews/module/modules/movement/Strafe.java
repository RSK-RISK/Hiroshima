package me.rsk.hiroshima.jews.module.modules.movement;

import me.rsk.hiroshima.jews.module.Module;

//Cred to Rina
@Module.Info(name = "Strafe", description = "Strafe: only one speed for jump motion.", category = Module.Category.MOVEMENT)
public class Strafe extends Module {
    float player_forward;
    float player_strafe;
    float player_pitch;
    float player_yaw;

    @Override
    public void onUpdate() {
        player_forward = mc.player.movementInput.moveForward;
        player_strafe = mc.player.movementInput.moveStrafe;
        player_pitch = mc.player.rotationPitch;
        player_yaw = mc.player.rotationYaw;

        if (player_forward == 0.0f && player_strafe == 0.0f) {
            mc.player.motionX *= (0.0f);
            mc.player.motionZ *= (0.0f);

        } else {
            if (player_forward != 0.0f) {
                if (player_strafe > 0.0f) {
                    player_yaw += (player_strafe > 0.0f) ? -45 : 45;

                } else if (player_strafe < 0.0f) {
                    player_yaw += (player_strafe > 0.0f) ? 45 : -45;
                }

                player_strafe = 0.0f;

                if (player_forward > 0.0f) {
                    player_forward = 1.0f;
                } else if (player_forward < 0.0f) {
                    player_forward = - 1.0f;
                }
            }
        }

        mc.player.motionX = ((player_forward * 0.2000f) * Math.cos(Math.toRadians((player_yaw + 90.0f))) + (player_strafe * 0.2000f) * Math.sin(Math.toRadians((player_yaw + 90.0f))));
        mc.player.motionZ = ((player_forward * 0.2000f) * Math.sin(Math.toRadians((player_yaw + 90.0f))) - (player_strafe * 0.2000f) * Math.cos(Math.toRadians((player_yaw + 90.0f))));
    }
}