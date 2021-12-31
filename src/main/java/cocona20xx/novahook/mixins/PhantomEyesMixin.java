package cocona20xx.novahook.mixins;

import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.PhantomEyesFeatureRenderer;
import net.minecraft.client.render.entity.model.PhantomEntityModel;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PhantomEyesFeatureRenderer.class)
public abstract class PhantomEyesMixin<T extends PhantomEntity> extends EyesFeatureRenderer<T, PhantomEntityModel<T>> implements FeatureRendererAccessor {
    @Unique private static final Identifier TEXTURE_POINTER = new Identifier("textures/entity/phantom_eyes.png");
    public PhantomEyesMixin(FeatureRendererContext<T, PhantomEntityModel<T>> featureRendererContext) {
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
