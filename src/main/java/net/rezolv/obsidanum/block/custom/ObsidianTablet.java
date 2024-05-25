package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.item.ItemsObs;

public class ObsidianTablet extends Block {
    private static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(2.0, 0.0, 6.0, 14.0, 23.0, 10.0);
    private static final VoxelShape SHAPE_EAST_WEST = Block.box(6.0, 0.0, 2.0, 10.0, 23.0, 14.0);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty EXPERIENCED = BooleanProperty.create("experienced");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public ObsidianTablet(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EXPERIENCED, false).setValue(ACTIVE,false)); // Устанавливаем начальное состояние блока: активность = false

    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? SHAPE_NORTH_SOUTH : SHAPE_EAST_WEST;
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        super.attack(pState, pLevel, pPos, pPlayer);
        if (!pState.getValue(EXPERIENCED)) {
            ((Level)pLevel).explode(null, pPos.getX(), pPos.getY(), pPos.getZ(), 3, Level.ExplosionInteraction.TNT);
        }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Получаем предмет, который будет установлен
        ItemStack stack = context.getItemInHand();

        // Проверяем, содержит ли предмет теги
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            int customModelData = tag.getInt("CustomModelData");

            // Проверяем значение тега CustomModelData
            boolean experienced = customModelData == 1 || customModelData == 2;
            boolean active = customModelData == 2;

            // Создаем новое состояние блока с учетом переданных тегов
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(EXPERIENCED, experienced)
                    .setValue(ACTIVE, active);
        }

        // Возвращаем состояние блока по умолчанию, если теги не найдены
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXPERIENCED, ACTIVE);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        // Call the parent class method first
        boolean result = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

        // Check if the block should be harvested and the player is not in creative mode
        if (!player.isCreative() && result && canHarvestBlock(state, level, pos, player)) {
            ItemStack itemStack = new ItemStack(this.asItem());
            // Получаем инструмент, который использует игрок
            ItemStack tool = player.getMainHandItem();
            boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
            // Create a new tag for the item stack
            CompoundTag compoundTag = new CompoundTag();
            itemStack.setTag(compoundTag);

            // Set CustomModelData based on the block state
            if (state.getValue(EXPERIENCED) && state.getValue(ACTIVE) && hasSilkTouch) {
                compoundTag.putBoolean("experienced", state.getValue(EXPERIENCED));
                compoundTag.putBoolean("active", state.getValue(EXPERIENCED));

                itemStack.getOrCreateTag().putInt("CustomModelData", 2);
            } else {
                compoundTag.putBoolean("experienced2", state.getValue(EXPERIENCED));
                itemStack.getOrCreateTag().putInt("CustomModelData", 1);
            }

            // Drop the item stack
            popResource(level, pos, itemStack);
        }

        return result;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack itemStack = new ItemStack(this.asItem());

        // Создаем новый тег для предмета и присваиваем CustomModelData значение по умолчанию
        CompoundTag compoundTag = new CompoundTag();
        itemStack.setTag(compoundTag);
        itemStack.getOrCreateTag().putInt("CustomModelData", 0);

        // Устанавливаем CustomModelData на основе состояния блока
        if (state.getValue(EXPERIENCED) && state.getValue(ACTIVE)) {
            compoundTag.putBoolean("experienced", state.getValue(EXPERIENCED));
            compoundTag.putBoolean("active", state.getValue(ACTIVE));
            itemStack.getOrCreateTag().putInt("CustomModelData", 2);
        } else if (state.getValue(EXPERIENCED)) {
            compoundTag.putBoolean("experienced", state.getValue(EXPERIENCED));
            itemStack.getOrCreateTag().putInt("CustomModelData", 1);
        }

        return itemStack;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        ItemStack tool = player.getMainHandItem();
        if (tool.getItem() instanceof TieredItem) {
            TieredItem tieredItem = (TieredItem) tool.getItem();
            int toolLevel = tieredItem.getTier().getLevel();

            return toolLevel >= 3; // Проверяем, что инструмент имеет уровень добычи 3 (алмазный) или выше
        }
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            ItemStack itemInHand = player.getItemInHand(hand);

            // Check if player is using the Obsidian Tear and the block is in the correct state
            if (itemInHand.getItem() == ItemsObs.OBSIDIAN_TEAR.get() && state.getValue(EXPERIENCED) && !state.getValue(ACTIVE)) {
                world.setBlock(pos, state.setValue(ACTIVE, true), 3);

                // Play activation sound
                world.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, player.getSoundSource(), 1.0F, 1.0F);

                // Spawn happy villager particles
                if (world instanceof ServerLevel) {
                    ServerLevel serverWorld = (ServerLevel) world;
                    for (int i = 0; i < 10; i++) {
                        double offsetX = world.random.nextGaussian() * 0.5D;
                        double offsetY = world.random.nextGaussian() * 0.5D;
                        double offsetZ = world.random.nextGaussian() * 0.5D;
                        serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 5, offsetX, offsetY, offsetZ, 1.0D);
                    }
                }
                // Consume one Obsidian Tear
                itemInHand.shrink(1);
                return InteractionResult.SUCCESS;
            }

            // Existing interaction logic
            if (hand == InteractionHand.MAIN_HAND && !state.getValue(EXPERIENCED)) {
                world.setBlock(pos, state.setValue(EXPERIENCED, true), 3);

                // Spawn lightning bolt
                LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                world.addFreshEntity(lightning);

                // Schedule experience drop
                if (world instanceof ServerLevel) {
                    ServerLevel serverWorld = (ServerLevel) world;
                    serverWorld.scheduleTick(pos, this, 25); // 10 ticks = 0.5 seconds
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(ACTIVE) ? 6 : 0;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockBelow = world.getBlockState(blockpos);
        return Block.isFaceFull(blockBelow.getCollisionShape(world, blockpos), Direction.UP) ;
    }
    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if (pState.getValue(EXPERIENCED)) {
            // Drop experience
            ExperienceOrb.award(pLevel, Vec3.atCenterOf(pPos), 300);
        }
    }
}