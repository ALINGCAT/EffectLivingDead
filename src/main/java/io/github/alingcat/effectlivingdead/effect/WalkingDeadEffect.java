package io.github.alingcat.effectlivingdead.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public class WalkingDeadEffect extends MobEffect {
    private static final ResourceLocation ICON = new ResourceLocation("effectlivingdead", "textures/mob_effect/walking_dead.png");

    public WalkingDeadEffect() {
        super(MobEffectCategory.NEUTRAL, 0x8E8E8E);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new IClientMobEffectExtensions() {
            @Override
            public boolean renderInventoryIcon(MobEffectInstance effect, EffectRenderingInventoryScreen<?> screen, GuiGraphics gui,
                                               int x, int y, int blitOffset) {
                var minecraft = Minecraft.getInstance();
                gui.pose().pushPose();
                gui.pose().translate(0, 0, 200);
                gui.blit(ICON, x, y+7, 0, 0, 18, 18, 18, 18);
                gui.pose().popPose();
                return true;
            }

            @Override
            public boolean renderGuiIcon(MobEffectInstance effect, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
                guiGraphics.blit(ICON, x+3, y+3, (int)z, 0, 0, 18, 18, 18, 18);
                return true;
            }
        });
    }
}
