package cocona20xx.novahook.internal;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.CustomEyeLayerDuckInterface;
import cocona20xx.novahook.internal.accessors.VanillaEyeLayerDuckInterface;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public final class MultiEntityCustomEyeLayerFeature<T extends Entity, M extends EntityModel<T>> extends EyesFeatureRenderer<T, M> implements CustomEyeLayerDuckInterface, VanillaEyeLayerDuckInterface {
    public static final Identifier EMPTY_TEXTURE_REF = new Identifier("novahook", "textures/empty_sprite.png");
    private Identifier currentEyesTexture = null;
    private Identifier fallbackEyesTexture = EMPTY_TEXTURE_REF;
    private boolean isReplacement = false;
    private OverrideToken token = null;


    public MultiEntityCustomEyeLayerFeature(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
        this.currentEyesTexture = EMPTY_TEXTURE_REF;
    }

    public MultiEntityCustomEyeLayerFeature(FeatureRendererContext<T, M> featureRendererContext, Identifier initialEyeTexture){
        super(featureRendererContext);
        this.currentEyesTexture = initialEyeTexture;
        this.fallbackEyesTexture = initialEyeTexture;
        this.isReplacement = true;
    }

    public boolean isRendering(){
        return ((this.currentEyesTexture != null) && !(Objects.equals(currentEyesTexture, EMPTY_TEXTURE_REF)));
    }

    @Override
    public RenderLayer getEyesTexture() {
        return RenderLayer.getEyes(currentEyesTexture);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(entity);
        if(token == null) {
            if (isReplacement)
                token = new OverrideToken(accessed.getTypeId(), currentEyesTexture, OverrideToken.TokenTypes.eye_layer);
            else
                token = new OverrideToken(accessed.getTypeId(), OverrideToken.MULTI_EYE_POINTER_ID, OverrideToken.TokenTypes.eye_layer);
            accessed.storeToken(token);
        }
        currentEyesTexture = accessed.retrieveOverride(token).orElse(fallbackEyesTexture);
        if(this.isRendering()){
            VertexConsumer consumer = vertexConsumers.getBuffer(this.getEyesTexture());
            this.getContextModel().render(matrices, consumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    @Override
    public boolean duck() {
        return true;
    }

    @Override
    public boolean duckVanilla(){
        return false;
    }
}
