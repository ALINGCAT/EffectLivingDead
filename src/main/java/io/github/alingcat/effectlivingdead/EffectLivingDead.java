package io.github.alingcat.effectlivingdead;

import io.github.alingcat.effectlivingdead.effect.LivingDeadEffect;
import io.github.alingcat.effectlivingdead.effect.UndeadRebirthEffect;
import io.github.alingcat.effectlivingdead.effect.WalkingDeadEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "effectlivingdead");

    public static final RegistryObject<Item> UNDEAD_FRAGMENT = ITEMS.register("undead_fragment",
            () -> new Item(new Item.Properties()));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "effectlivingdead");

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("EffectLivingDead", () -> {
        var builder = CreativeModeTab.builder();
        builder.title(Component.translatable("effect.effectlivingdead.living_dead"));
        builder.icon(() -> new ItemStack(UNDEAD_FRAGMENT.get()));
        var potionStack = new ItemStack(Items.POTION);
        var splashPotionStack = new ItemStack(Items.SPLASH_POTION);
        PotionUtils.setPotion(potionStack, LIVING_DEAD_POTION.get());
        PotionUtils.setPotion(splashPotionStack, LIVING_DEAD_POTION.get());
        builder.displayItems((parameters, output) -> {
            output.accept(UNDEAD_FRAGMENT.get());
            output.accept(potionStack);
            output.accept(splashPotionStack);
        });
        return builder.build();
    });

    public EffectLivingDead() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        EFFECTS.register(bus);
        POTIONS.register(bus);
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);

        bus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        var awkward = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        var base = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), LIVING_DEAD_POTION.get()));
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(awkward, Ingredient.of(UNDEAD_FRAGMENT.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), LIVING_DEAD_POTION.get()));
            BrewingRecipeRegistry.addRecipe(base, Ingredient.of(Items.GUNPOWDER),
                    PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), LIVING_DEAD_POTION.get()));
        });
    }
}

