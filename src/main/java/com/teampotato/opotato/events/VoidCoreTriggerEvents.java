package com.teampotato.opotato.events;

import L_Ender.cataclysm.init.ModItems;
import L_Ender.cataclysm.items.void_core;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.api.cataclysm.IVoidCore;
import com.teampotato.opotato.config.mods.CataclysmExtraConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class VoidCoreTriggerEvents {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level;
        if (event.phase == TickEvent.Phase.END && player.removeTag(Opotato.MOD_ID + ".voidCore") && !world.isClientSide) {
            CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.VOID_CORE.get()).ifPresent(slotResult -> {
                ItemStack stack = slotResult.getStack();
                void_core voidCore = (void_core) stack.getItem();
                int standingOnY = Mth.floor(player.getY()) - 1;
                double headY = player.getY() + 1.0;
                float yawRadians = (float)Math.toRadians(90.0F + player.yRot);
                boolean hasSucceeded = false;
                int k;
                if (player.xRot > 70.0F) {
                    float rotatedYaw;
                    for(k = 0; k < 5; ++k) {
                        rotatedYaw = yawRadians + (float)k * 3.1415927F * 0.4F;
                        if (((IVoidCore)voidCore)._spawnFangs(player.getX() + (double)Mth.cos(rotatedYaw) * 1.5, headY, player.getZ() + (double)Mth.sin(rotatedYaw) * 1.5, standingOnY, rotatedYaw, 0, world, player)) {
                            hasSucceeded = true;
                        }
                    }

                    for(k = 0; k < 8; ++k) {
                        rotatedYaw = yawRadians + (float)k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                        if (((IVoidCore)voidCore)._spawnFangs(player.getX() + (double)Mth.cos(rotatedYaw) * 2.5, headY, player.getZ() + (double)Mth.sin(rotatedYaw) * 2.5, standingOnY, rotatedYaw, 3, world, player)) {
                            hasSucceeded = true;
                        }
                    }
                } else {
                    for(k = 0; k < 10; ++k) {
                        double d2 = 1.25 * (double)(k + 1);
                        if (((IVoidCore)voidCore)._spawnFangs(player.getX() + (double)Mth.cos(yawRadians) * d2, headY, player.getZ() + (double)Mth.sin(yawRadians) * d2, standingOnY, yawRadians, k, world, player)) {
                            hasSucceeded = true;
                        }
                    }
                }

                if (hasSucceeded) {if (CataclysmExtraConfig.voidCoreCanBeDamaged.get()) {
                    int damage = stack.getDamageValue();
                    if (damage + 1 >= CataclysmExtraConfig.voidCoreDurability.get()) {
                        stack.shrink(1);
                        return;
                    } else {
                        voidCore.setDamage(stack, damage + 1);
                    }
                }
                    player.getCooldowns().addCooldown(voidCore, CataclysmExtraConfig.voidCoreCoolDown.get());
                }
            });
        }
    }
}
