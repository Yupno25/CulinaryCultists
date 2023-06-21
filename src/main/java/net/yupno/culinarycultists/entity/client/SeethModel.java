package net.yupno.culinarycultists.entity.client;


import net.minecraft.resources.ResourceLocation;
import net.yupno.culinarycultists.CulinaryCultists;
import net.yupno.culinarycultists.entity.custom.SeethEntity;
import software.bernie.geckolib.model.GeoModel;

public class SeethModel extends GeoModel<SeethEntity> {
    @Override
    public ResourceLocation getModelResource(SeethEntity object) {
        return new ResourceLocation(CulinaryCultists.MOD_ID, "geo/seeth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeethEntity object) {
        return new ResourceLocation(CulinaryCultists.MOD_ID, "textures/entity/seeth_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeethEntity animatable) {
        return new ResourceLocation(CulinaryCultists.MOD_ID, "animations/seeth.animation.json");
    }
}
