package cocona20xx.novahook.internal;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public final class MultiEntityEyeLayerFeature<T extends Entity, M extends EntityModel<T>> extends EyesFeatureRenderer<T, M> implements EyeLayerAccessor{
    private Identifier currentEyesTexture = new Identifier("novahook", "textures/empty_sprite.png");
    private boolean shouldRender = false;

    public MultiEntityEyeLayerFeature(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }


    public void enableEyeLayer(Identifier withThisTexture){
        currentEyesTexture = withThisTexture;
        shouldRender = true;
    }

    public void disableEyeLayer(){
        shouldRender = false;
    }

    @Override
    public RenderLayer getEyesTexture() {
        return RenderLayer.getEyes(currentEyesTexture);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(shouldRender) {
            super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }
}
