package net.rezolv.obsidanum.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

import java.lang.reflect.Method;

public class ModFlammableBlocks {

    public static void registerFlammableBlocks() {
        try {
            // Получаем метод setFlammable из класса FireBlock через рефлексию
            Method setFlammableMethod = FireBlock.class.getDeclaredMethod("setFlammable", Block.class, int.class, int.class);
            // Делаем метод доступным для вызова
            setFlammableMethod.setAccessible(true);
            // Получаем экземпляр блока огня (FireBlock)
            FireBlock fireBlock = (FireBlock) Blocks.FIRE;
            // Устанавливаем горючие свойства для пользовательского блока APPLE_LOG:
            // - 5: вероятность воспламенения (Ignite Odds). Низкая вероятность для древесины.
            //       0: блок никогда не загорается (например, камень).
            //       60: блок очень легко воспламеняется (например, лиственница или шерсть).
            // - 20: скорость распространения огня (Burn Odds). Средняя скорость для древесных блоков.
            //       0: огонь не распространяется через блок (например, камень).
            //       60: огонь распространяется очень быстро (например, листья).

            setFlammableMethod.invoke(fireBlock, BlocksObs.OBSIDAN_FENCE.get(), 5, 20);
            setFlammableMethod.invoke(fireBlock, BlocksObs.OBSIDAN_FENCE_GATE.get(), 5, 20);
            setFlammableMethod.invoke(fireBlock, BlocksObs.BLOCK_OF_STITCHED_LEATHER.get(), 10, 35);
        } catch (Exception e) {
            // Если происходит ошибка (например, метод не найден), она будет выведена в консоль
            e.printStackTrace();
        }
    }
}