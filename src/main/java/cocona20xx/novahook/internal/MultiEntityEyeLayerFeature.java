package cocona20xx.novahook.internal;

import lombok.Setter;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public final class MultiEntityEyeLayerFeature<T extends Entity, M extends EntityModel<T>> extends EyesFeatureRenderer<T, M> {

    public MultiEntityEyeLayerFeature(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Setter private Identifier currentEyesTexture = new Identifier("novahook", "textures/empty_sprite.png");

    @Override
    public RenderLayer getEyesTexture() {
        return RenderLayer.getEyes(currentEyesTexture);
    }
}
