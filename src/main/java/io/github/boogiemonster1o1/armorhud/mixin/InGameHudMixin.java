package io.github.boogiemonster1o1.armorhud.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method="render",at=@At(value="INVOKE",target = "Lcom/mojang/blaze3d/platform/GlStateManager;color4f(FFFF)V",ordinal = 2))
    private void renderArmor(float tickDelta,CallbackInfo ci){
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player != null && mc.player.inventory != null){
            GlStateManager.enableRescaleNormal();
            GlStateManager.clearColor();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            GuiLighting.method_2214();
            for (int i = 0; i < 4; i++) {
                int y = MinecraftClient.getInstance().height - 40 - i * 20;
                renderArmorProper(i, 5, y, tickDelta, (PlayerEntity)mc.player);
            }
            GuiLighting.method_2210();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    private void renderArmorProper(int slot, int x, int y, float tickDelta, PlayerEntity player){
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack armor = player.inventory.armor[slot];
        if (armor != null) {
            float frames = armor.field_8654 - tickDelta;
            if (frames > 0.0F) {
                GlStateManager.pushMatrix();
                float var8 = 1.0F + frames / 5.0F;
                GlStateManager.translatef((x + 8), (y + 12), 0.0F);
                GlStateManager.scalef(1.0F / var8, (var8 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translatef(-(x + 8), -(y + 12), 0.0F);
            }
            itemRenderer.renderInGuiWithOverrides(armor, x, y);
            if (frames > 0.0F)
                GlStateManager.popMatrix();
            itemRenderer.method_3966(MinecraftClient.getInstance().textRenderer, armor, x, y);
        }
    }
}
