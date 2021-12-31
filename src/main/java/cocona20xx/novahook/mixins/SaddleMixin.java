package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Saddleable;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaddleFeatureRenderer.class)
public abstract class SaddleMixin <T extends Entity & Saddleable, M extends EntityModel<T>> extends FeatureRenderer<T, M> implements FeatureRendererAccessor {
    @Shadow @Final private Identifier TEXTURE;
    @Shadow @Final private M model;
    @Unique private OverrideToken token = null;

    public SaddleMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public Identifier getOriginal() {
        return TEXTURE;
    }
    @Inject(method = "render",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci){
        if(entity.isSaddled()) {
            EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(entity);
            if(token == null){
                token = new OverrideToken(accessed.getTypeId(), this.getOriginal(), OverrideToken.TokenTypes.feature);
                accessed.storeToken(token);
            }
            if(accessed.retrieveOverride(token).isPresent()){
                Identifier newSaddle = accessed.retrieveOverride(token).get();
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(newSaddle));
                model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                ci.cancel();
            }
        }
    }
}
