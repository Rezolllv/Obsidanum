package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.entity.RightForgeScrollEntity;
import org.jetbrains.annotations.Nullable;


public class ForgeCrucible extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ForgeCrucible(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {



        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        Direction facing = state.getValue(FACING);
        BlockPos rightPos;
        switch (facing) {
            case NORTH -> rightPos = pos.west();
            case SOUTH -> rightPos = pos.east();
            case EAST -> rightPos = pos.north();
            case WEST -> rightPos = pos.south();
            default -> {
                System.out.println("Unknown facing direction!");
                return;
            }
        }

        BlockEntity rightEntity = level.getBlockEntity(rightPos);
        if (rightEntity instanceof RightForgeScrollEntity rightForgeScrollEntity) {
            CompoundTag scrollNBT = rightForgeScrollEntity.getScrollNBT();

            if (!scrollNBT.isEmpty()) {
                System.out.println("RightForgeScroll NBT Data at " + rightPos + ": " + scrollNBT);

                // Извлечение информации о рецепте
                if (scrollNBT.contains("type")) {
                    String type = scrollNBT.getString("type");
                    System.out.println("Recipe Type: " + type);
                }

                if (scrollNBT.contains("ingredients", Tag.TAG_LIST)) {
                    ListTag ingredientsList = scrollNBT.getList("ingredients", Tag.TAG_COMPOUND);
                    System.out.println("Ingredients:");
                    for (Tag ingredientTag : ingredientsList) {
                        CompoundTag ingredientNBT = (CompoundTag) ingredientTag;
                        String item = ingredientNBT.getString("item");
                        int count = ingredientNBT.getInt("count");
                        System.out.println("- Item: " + item + ", Count: " + count);
                    }
                }
                if (!scrollNBT.contains("output", Tag.TAG_COMPOUND)) {
                    BlockEntity crucibleEntity = level.getBlockEntity(pos);
                    if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                        forgeCrucibleEntity.setOutput(ItemStack.EMPTY);
                        forgeCrucibleEntity.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                    }
                }
                if (scrollNBT.contains("RecipeResult", Tag.TAG_LIST)) {
                    ListTag recipeResultList = scrollNBT.getList("RecipeResult", Tag.TAG_COMPOUND);
                    if (!recipeResultList.isEmpty()) {
                        CompoundTag outputTag = recipeResultList.getCompound(0); // Берем первый элемент
                        String itemId = outputTag.getString("id");
                        int count = outputTag.getInt("Count");
                        Item outputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                        if (outputItem != null) {
                            ItemStack output = new ItemStack(outputItem, count);

                            BlockEntity crucibleEntity = level.getBlockEntity(pos);
                            if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                                forgeCrucibleEntity.setOutput(output);
                                forgeCrucibleEntity.setChanged();
                                level.sendBlockUpdated(pos, state, state, 3);
                            }
                            System.out.println("Set output: " + output);
                        }
                    }
                }



                BlockEntity crucibleEntity = level.getBlockEntity(pos);
                if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                    forgeCrucibleEntity.updateFromScrollNBT(scrollNBT);
                    forgeCrucibleEntity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            } else if (scrollNBT.isEmpty()) {
                // Print a message indicating the ingredients list is empty
                System.out.println("RightForgeScroll NBT Data is empty at " + rightPos);
                System.out.println("Ingredients list is empty.");

                // Get the BlockEntity for the current position
                BlockEntity crucibleEntity = level.getBlockEntity(pos);
                if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                    // Update the crucible entity (you can decide what specific update is needed)
                    forgeCrucibleEntity.clear(); // Assuming there's a method to clear ingredients
                    forgeCrucibleEntity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            } else {
                System.out.println("RightForgeScroll NBT Data is empty at " + rightPos);
            }
        }
    }


    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ForgeCrucibleEntity(pPos, pState);
    }
}
