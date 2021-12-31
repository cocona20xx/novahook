package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.ShulkerHeadFeatureRenderer;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ShulkerHeadFeatureRenderer.class)
public abstract class ShulkerHeadMixin extends FeatureRenderer<ShulkerEntity, ShulkerEntityModel<ShulkerEntity>> implements FeatureRendererAccessor {

    @Unique private Identifier lastShulkerId = null;
    @Unique private Identifier currentShulkerId = null;
    @Unique private OverrideToken token = null;

    public ShulkerHeadMixin(FeatureRendererContext<ShulkerEntity, ShulkerEntityModel<ShulkerEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/mob/ShulkerEntity;FFFFFF)V",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ShulkerEntity shulkerEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(shulkerEntity);
        currentShulkerId = ShulkerEntityRenderer.getTexture(shulkerEntity.getColor());
        if(!Objects.equals(currentShulkerId, lastShulkerId)){
            if(token != null) accessed.clearToken(token);
            token = null;
        }
        if(token == null){
            token = new OverrideToken(accessed.getTypeId(), this.currentShulkerId, OverrideToken.TokenTypes.feature);
            accessed.storeToken(token);
        }
        lastShulkerId = currentShulkerId;
        if(accessed.retrieveOverride(token).isPresent()){
            Identifier newId = accessed.retrieveOverride(token).get();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(newId));
            getContextModel().getHead().render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(shulkerEntity, 0.0F));
            ci.cancel();
        }
    }

    @Override
    public Identifier getOriginal() {
        return currentShulkerId;
    }
}
