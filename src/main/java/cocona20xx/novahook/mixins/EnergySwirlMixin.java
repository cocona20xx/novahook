package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnergySwirlOverlayFeatureRenderer.class)
public abstract class EnergySwirlMixin <T extends Entity & SkinOverlayOwner, M extends EntityModel<T>> extends FeatureRenderer<T, M> implements FeatureRendererAccessor {
    @Shadow protected abstract Identifier getEnergySwirlTexture();
    @Shadow protected abstract float getEnergySwirlX(float partialAge);
    @Unique private OverrideToken token = null;

    public EnergySwirlMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true
    )
    public void onRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch,
                         CallbackInfo ci, float f, EntityModel<T> entityModel, VertexConsumer vertexConsumer){
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(entity);
        if(token == null) {
            token = new OverrideToken(accessed.getTypeId(), this.getOriginal(), OverrideToken.TokenTypes.feature);
            accessed.storeToken(token);
        }
        if(accessed.retrieveOverride(token).isPresent() && entity.shouldRenderOverlay()){
            VertexConsumer newConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(accessed.retrieveOverride(token).get(), this.getEnergySwirlX(f) % 1.0F, f * 0.01F % 1.0F));
            entityModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            entityModel.render(matrices, newConsumer, light, OverlayTexture.DEFAULT_UV, 0.5F, 0.5F, 0.5F, 1.0F);
            ci.cancel();
        }
    }

    @Override
    public Identifier getOriginal() {
        return this.getEnergySwirlTexture();
    }
}
