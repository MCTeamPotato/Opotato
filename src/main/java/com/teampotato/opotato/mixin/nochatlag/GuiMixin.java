package com.teampotato.opotato.mixin.nochatlag;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mixin(net.minecraft.client.gui.IngameGui.class)
public abstract class GuiMixin {

    private final ExecutorService service = Executors.newFixedThreadPool(1);

    @Final
    @Shadow
    protected Minecraft minecraft;

    @Final
    @Shadow
    protected Map<ChatType, List<IChatListener>> chatListeners;

    @Shadow
    public abstract UUID guessChatUUID(ITextComponent message);

    @Inject(
            method = "handleChat(Lnet/minecraft/util/text/ChatType;Lnet/minecraft/util/text/ITextComponent;Ljava/util/UUID;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void handleChat(ChatType chatType, ITextComponent chatComponent, UUID senderUUID, CallbackInfo ci) {
        service.submit(() -> {
            if (this.minecraft.isBlocked(senderUUID) || (this.minecraft.options.hideMatchedNames && this.minecraft.isBlocked(this.guessChatUUID(chatComponent)))) {
                return;
            }
            for (IChatListener chatListener : this.chatListeners.get(chatType)) {
                chatListener.handle(chatType, chatComponent, senderUUID);
            }
        });
        ci.cancel();
    }
}
