package net.aiden.gaymod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoofItem extends Item {
    public BoofItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND)
        {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200));
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200));
        }

        return super.use(level, player, hand);
    }
}
