package com.teampotato.opotato.mixin.block_limit_fix;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SMultiBlockChangePacket;
import net.minecraft.util.math.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SMultiBlockChangePacket.class, priority = 100)
public abstract class ClientboundSectionBlocksUpdatePacketMixin implements IPacket<IClientPlayNetHandler> {
    @Shadow
    private SectionPos sectionPos;

    @Shadow
    private boolean suppressLightUpdates;

    @Shadow
    private short[] positions;

    @Shadow
    private BlockState[] states;

    /**
     * @author TropheusJay
     * @reason fix a vanilla bug with state serialization
     */
    @Overwrite
    @Override
    public void write(PacketBuffer buf) {
        buf.writeLong(this.sectionPos.asLong());
        buf.writeBoolean(this.suppressLightUpdates);
        buf.writeVarInt(this.positions.length);

        for(int i = 0; i < this.positions.length; ++i) {
            int id = Block.getId(this.states[i]);
            buf.writeVarLong(((long) id) << 12 | this.positions[i]);
        }
    }
}
