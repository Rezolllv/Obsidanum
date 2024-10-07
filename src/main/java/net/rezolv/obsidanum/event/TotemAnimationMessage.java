package net.rezolv.obsidanum.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.rezolv.obsidanum.item.ItemsObs;

import java.util.function.Supplier;

public class TotemAnimationMessage {
    public TotemAnimationMessage() {
        // Конструктор по умолчанию
    }

    public static void encode(TotemAnimationMessage msg, FriendlyByteBuf buf) {
        // Кодировать данные в буфер, если нужно
    }

    public static TotemAnimationMessage decode(FriendlyByteBuf buf) {
        return new TotemAnimationMessage(); // Декодировать данные из буфера, если нужно
    }

    public static void handle(TotemAnimationMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Воспроизвести анимацию на клиенте
            Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(ItemsObs.OBSIDIAN_TOTEM_OF_IMMORTALITY.get()));
        });
        context.setPacketHandled(true);
    }
}