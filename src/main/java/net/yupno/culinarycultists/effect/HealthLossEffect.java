package net.yupno.culinarycultists.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class HealthLossEffect extends MobEffect{
    public HealthLossEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);

        if(pLivingEntity.getHealth() > pLivingEntity.getMaxHealth())
            pLivingEntity.setHealth(pLivingEntity.getMaxHealth());
    }
}
