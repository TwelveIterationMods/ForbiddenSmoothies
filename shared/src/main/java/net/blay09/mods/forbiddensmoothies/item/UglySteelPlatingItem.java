package net.blay09.mods.forbiddensmoothies.item;

import net.blay09.mods.forbiddensmoothies.block.CustomBlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class UglySteelPlatingItem extends Item {
    public UglySteelPlatingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final var level = context.getLevel();
        final var pos = context.getClickedPos();
        final var state = level.getBlockState(pos);
        if (state.hasProperty(CustomBlockStateProperties.UGLY)) {
            level.setBlockAndUpdate(pos, state.setValue(CustomBlockStateProperties.UGLY, true));
            if (!context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
