package io.github.alingcat.effectlivingdead;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "effectlivingdead")
public class EventHandler {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        var entity = event.getEntity();
        if (event.getAmount() >= entity.getHealth()) {
            if (entity.hasEffect(EffectLivingDead.LIVING_DEAD_EFFECT.get())) {
                event.setCanceled(true);
                entity.setHealth(1.0f);
                entity.removeEffect(EffectLivingDead.LIVING_DEAD_EFFECT.get());
                entity.addEffect(new MobEffectInstance(EffectLivingDead.WALKING_DEAD_EFFECT.get(), 200, 0));
            }
            else if (entity.hasEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get()) || entity.hasEffect(EffectLivingDead.UNDEAD_REBIRTH_EFFECT.get())) {
                event.setCanceled(true);
                entity.setHealth(1.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        var entity = event.getEntity();
        if (event.getAmount() > 7.99 && entity.hasEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get())) {
            var effect = entity.getEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get());
            int remaining = 0;
            if (effect != null) remaining = effect.getDuration();
            entity.removeEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get());
            entity.addEffect(new MobEffectInstance(EffectLivingDead.UNDEAD_REBIRTH_EFFECT.get(), remaining, 0));
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent event) {
        var entity = event.getEntity();
        var stack = event.getItem();
        var item = stack.getItem();
        if (entity.hasEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get())) {
            var regenration = false;
            if (item == Items.POTION) {
                for (var effect : PotionUtils.getMobEffects(stack)) {
                    if (effect.getEffect() == MobEffects.REGENERATION) {
                        regenration = true;
                        break;
                    }
                }
            }
            if (item == Items.GOLDEN_APPLE || item == Items.ENCHANTED_GOLDEN_APPLE) {
                var effect = entity.getEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get());
                int remaining = 0;
                if (effect != null) remaining = effect.getDuration();
                entity.removeEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get());
                entity.addEffect(new MobEffectInstance(EffectLivingDead.UNDEAD_REBIRTH_EFFECT.get(), remaining, 0));
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event) {
        var entity = event.getEntity();
        if (event.getEffectInstance().getEffect() == EffectLivingDead.WALKING_DEAD_EFFECT.get()) {
            entity.setHealth(0.0f);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Applicable event) {
        var entity = event.getEntity();
        if (event.getEffectInstance().getEffect() == EffectLivingDead.LIVING_DEAD_EFFECT.get() &&
                entity.hasEffect(EffectLivingDead.WALKING_DEAD_EFFECT.get()) || entity.hasEffect(EffectLivingDead.UNDEAD_REBIRTH_EFFECT.get())) {
            event.setResult(Event.Result.DENY);
        }
    }
}
