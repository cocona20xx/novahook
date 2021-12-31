package cocona20xx.novahook.mixins;

import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SpiderEyesFeatureRenderer;
import net.minecraft.client.render.entity.model.SpiderEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SpiderEyesFeatureRenderer.class)
public abstract class SpiderEyesMixin<T extends Entity, M extends SpiderEntityModel<T>> extends EyesFeatureRenderer<T, M> implements FeatureRendererAccessor {
    @Unique private static final Identifier TEXTURE_POINTER = new Identifier("textures/entity/spider_eyes.png");
    public SpiderEyesMixin(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    protected Identifier getTexture(T entity) {
        return TEXTURE_POINTER;
    }

    @Override
    public Identifier getOriginal() {
        return TEXTURE_POINTER;
    }
}
