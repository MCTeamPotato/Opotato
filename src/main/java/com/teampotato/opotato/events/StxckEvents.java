package com.teampotato.opotato.events;

import com.teampotato.opotato.config.mods.stxck.StxckCommonConfig;
import com.teampotato.opotato.util.stxck.Staaaaaaaaaaaack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import static com.teampotato.opotato.util.stxck.StxckUtil.*;

@Mod.EventBusSubscriber
public class StxckEvents {
    @SubscribeEvent
    public static void onEntityJoinLevel(@NotNull EntityJoinWorldEvent event) {
        EventHandler.onEntityCreate(event.getEntity(), () -> event.setCanceled(true));
    }

    public static class EventHandler {
        private static final StxckCommonConfig config = Staaaaaaaaaaaack.commonConfig;


        public static void onEntityCreate(Entity entity, Runnable eventCanceller) {
            if (!(entity instanceof ItemEntity) || !isMergable((ItemEntity) entity) || isBlackListItem(((ItemEntity) entity).getItem())) return;

            double h = config.getMaxMergeDistanceHorizontal();
            double v = config.getMaxMergeDistanceVertical();
            for (ItemEntity nearByEntity : entity.level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(h, Math.max(v, 0.5f), h), she -> entity != she && isMergable(she))) {
                tryToMerge(nearByEntity, (ItemEntity) entity);
                if (!entity.isAlive()) {
                    eventCanceller.run();
                    break;
                }
            }
        }
    }
}
