package net.yupno.culinarycultists.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlightEffect extends MobEffect{
    public FlightEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if(pLivingEntity instanceof Player player){
            player.getAbilities().mayfly = true;
            Level level = player.level;

            if (level.isClientSide)
                player.getAbilities().setFlyingSpeed(player.getAbilities().getFlyingSpeed() * (pAmplifier + 1));
            player.onUpdateAbilities();
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if(pLivingEntity instanceof Player player){
            player.getAbilities().mayfly = player.isCreative();
            player.getAbilities().flying = player.isCreative();
            Level level = player.level;

            if (level.isClientSide)
                player.getAbilities().setFlyingSpeed(0.05F);
            player.onUpdateAbilities();
        }
    }
}
