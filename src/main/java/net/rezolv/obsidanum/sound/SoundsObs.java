package net.rezolv.obsidanum.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;

public class SoundsObs {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Obsidanum.MOD_ID);

    public static final RegistryObject<SoundEvent> CHAKRAM_HIT = registerSoundEvents("chacram_hit");
    public static final RegistryObject<SoundEvent> CHAKRAM_TROWE = registerSoundEvents("chakram_trowe");
    public static final RegistryObject<SoundEvent> OPEN_OBSIDIAN_DOOR = registerSoundEvents("open_obsidian_door");
    public static final RegistryObject<SoundEvent> CLOSE_OBSIDIAN_DOOR = registerSoundEvents("close_obsidian_door");
    public static final RegistryObject<SoundEvent> LOCK = registerSoundEvents("lock");
    public static final RegistryObject<SoundEvent> OBSIDIAN_ELEMENTAL_AMBIENT = registerSoundEvents("obsidian_elemental_ambient");
    public static final RegistryObject<SoundEvent> OBSIDIAN_ELEMENTAL_HURT = registerSoundEvents("obsidian_elemental_hurt");
    public static final RegistryObject<SoundEvent> OBSIDIAN_ELEMENTAL_DEATH = registerSoundEvents("obsidian_elemental_death");
    public static final RegistryObject<SoundEvent> REZOLV_THE_TALE_OF_THE_VANISHED_ORDER = registerSoundEvents("rezolv_the_tale_of_the_vanished_order");





    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        ResourceLocation id = new ResourceLocation(Obsidanum.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
