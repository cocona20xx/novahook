package cocona20xx.novahook.mixins;

import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndermanEyesFeatureRenderer.class)
public abstract class EndermanEyesMixin<T extends LivingEntity> extends EyesFeatureRenderer<T, EndermanEntityModel<T>> implements FeatureRendererAccessor {
    @Unique private static final Identifier TEXTURE_POINTER = new Identifier("textures/entity/enderman/enderman_eyes.png");


    public EndermanEyesMixin(FeatureRendererContext<T, EndermanEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public Identifier getOriginal() {
        return TEXTURE_POINTER;
    }

    @Override
    protected Identifier getTexture(T entity) {
        return TEXTURE_POINTER;
    }
}
