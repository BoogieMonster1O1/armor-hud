package io.github.boogiemonster1o1.armorhud.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow @Final private ItemRenderer itemRenderer;

    @Shadow @Final private MinecraftClient client;

    @Inject(method="render",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;method_2421(Lnet/minecraft/client/util/Window;F)V"))
    private void renderArmorInjection(float tickDelta,CallbackInfo ci){
        this.renderArmor(new Window(this.client),tickDelta);
    }

    private void renderArmor(Window window,float tickDelta){
        if (this.client.getCameraEntity() instanceof PlayerEntity) {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            PlayerEntity playerEntity = (PlayerEntity)this.client.getCameraEntity();
            float g = zOffset;
            this.zOffset = -90.0F;
            this.zOffset = g;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            GuiLighting.method_2214();

            for(int j = 0; j < 4; ++j) {
                int k = window.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = 16;
                this.renderArmorProper(j, k, l, tickDelta, playerEntity);
            }

            GuiLighting.method_2210();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    private void renderArmorProper(int i, int j, int k, float f, PlayerEntity playerEntity) {
        ItemStack itemStack = playerEntity.inventory.armor[i];
        if (itemStack != null) {
            float g = (float)itemStack.field_8654 - f;
            if (g > 0.0F) {
                GlStateManager.pushMatrix();
                float h = 1.0F + g / 5.0F;
                GlStateManager.translatef((float)(j + 8), (float)(k + 12), 0.0F);
                GlStateManager.scalef(1.0F / h, (h + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translatef((float)(-(j + 8)), (float)(-(k + 12)), 0.0F);
            }

            this.itemRenderer.renderInGuiWithOverrides(itemStack, j, k);
            if (g > 0.0F) {
                GlStateManager.popMatrix();
            }

            this.itemRenderer.method_3966(this.client.textRenderer, itemStack, j, k);
        }
    }

//    @Inject(method="render",at=@At(value="TAIL"))
//    private void renderArmor(float tickDelta,CallbackInfo ci){
//        MinecraftClient h2104106cfe966ef = MinecraftClient.getInstance();
//        h2104106cfe966ef.profiler.push("armorhud");
//        GlStateManager.enableRescaleNormal();
//        GlStateManager.clearColor();
//        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
//        GuiLighting.method_2214();
//        for (int i = 0; i < 4; i++) {
//            int y = MinecraftClient.getInstance().height - 40 - i * 20;
//            int x = MinecraftClient.getInstance().width / 2 + (10 * (3 - i) - 15) * 2 - 10;
//            this.renderArmorProper(i, x,y, tickDelta, h2104106cfe966ef.player);
//        }
//        GuiLighting.method_2210();
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.disableBlend();
//        h2104106cfe966ef.profiler.pop();
//    }
//
//    private void renderArmorProper(int slot, int x,int y, float tickDelta, PlayerEntity player){
//        ItemStack armor = player.inventory.armor[slot];
//        if (armor != null) {
//            float frames = armor.field_8654 - tickDelta;
//            if (frames > 0.0F) {
//                GlStateManager.pushMatrix();
//                float var8 = 1.0F + frames / 5.0F;
//                GlStateManager.translatef((5 + 8), (y + 12), 0.0F);
//                GlStateManager.scalef(1.0F / var8, (var8 + 1.0F) / 2.0F, 1.0F);
//                GlStateManager.translatef(-(5 + 8), -(y + 12), 0.0F);
//            }
//            this.itemRenderer.renderInGuiWithOverrides(armor, 15, y);
//            if (frames > 0.0F) {
//                GlStateManager.popMatrix();
//            }
//            this.itemRenderer.renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, armor, x, y,null);
//        }
//    }
}
