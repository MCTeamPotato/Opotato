package com.teampotato.opotato.mixin.fast_projectile_fix;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SEntityVelocityPacket.class)
public class MixinSEntityVelocityPacket {
    @Shadow
    private int id;

    @Shadow
    private int xa;

    @Shadow
    private int ya;

    @Shadow
    private int za;

    @Inject(method = "Lnet/minecraft/network/play/server/SEntityVelocityPacket;<init>" +
            "(ILnet/minecraft/util/math/vector/Vector3d;)V", at = @At(value = "RETURN"))
    public void SEntityVelocityPacket(int entityId, Vector3d motionVector, CallbackInfo ci) {
        this.xa = (int)(motionVector.x * 8000.0D);
        this.ya = (int)(motionVector.y * 8000.0D);
        this.za = (int)(motionVector.z * 8000.0D);
    }

    @Inject(method = "read" +
            "(Lnet/minecraft/network/PacketBuffer;)V", at = @At(value = "HEAD"), cancellable = true)
    public void readPacketData(PacketBuffer buf, CallbackInfo ci) {
        ci.cancel();
        this.id = buf.readVarInt();
        this.xa = buf.readInt();
        this.ya = buf.readInt();
        this.za = buf.readInt();
    }

    @Inject(method = "write" +
            "(Lnet/minecraft/network/PacketBuffer;)V", at = @At(value = "HEAD"), cancellable = true)
    public void writePacketData(PacketBuffer buf, CallbackInfo ci) {
        ci.cancel();
        buf.writeVarInt(this.id);
        buf.writeInt(this.xa);
        buf.writeInt(this.ya);
        buf.writeInt(this.za);
    }
}
