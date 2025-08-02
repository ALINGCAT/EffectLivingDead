package io.github.alingcat.effectlivingdead;

import io.github.alingcat.effectlivingdead.effect.LivingDeadEffect;
import io.github.alingcat.effectlivingdead.effect.UndeadRebirthEffect;
import io.github.alingcat.effectlivingdead.effect.WalkingDeadEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("effectlivingdead")
public class EffectLivingDead {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "effectlivingdead");

    public static final RegistryObject<MobEffect> LIVING_DEAD_EFFECT =
            EFFECTS.register("living_dead", LivingDeadEffect::new);

    public static final RegistryObject<MobEffect> WALKING_DEAD_EFFECT =
            EFFECTS.register("walking_dead", WalkingDeadEffect::new);

    public static final RegistryObject<MobEffect> UNDEAD_REBIRTH_EFFECT =
            EFFECTS.register("undead_rebirth", UndeadRebirthEffect::new);

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, "effectlivingdead");

    public static final RegistryObject<Potion> LIVING_DEAD_POTION = POTIONS.register("living_dead_potion",
            () -> new Potion(new MobEffectInstance(LIVING_DEAD_EFFECT.get(), 200)));

    public EffectLivingDead() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        EFFECTS.register(bus);
        POTIONS.register(bus);

        bus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        var awkward = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        var base = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), LIVING_DEAD_POTION.get()));
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(awkward, Ingredient.of(Items.CRIMSON_FUNGUS),
                PotionUtils.setPotion(new ItemStack(Items.POTION), LIVING_DEAD_POTION.get()));
            BrewingRecipeRegistry.addRecipe(base, Ingredient.of(Items.GUNPOWDER),
                    PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), LIVING_DEAD_POTION.get()));
        });
    }
}

