package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.DrownedOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedOverlayFeatureRenderer.class)
public abstract class DrownedOverlayMixin<T extends DrownedEntity> extends FeatureRenderer<T, DrownedEntityModel<T>> implements FeatureRendererAccessor {
    @Shadow @Final private static Identifier SKIN;
    @Shadow @Final private DrownedEntityModel<T> model;

    @Shadow public abstract void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T drownedEntity, float f, float g, float h, float j, float k, float l);

    @Unique private OverrideToken token = null;

    public DrownedOverlayMixin(FeatureRendererContext<T, DrownedEntityModel<T>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/mob/DrownedEntity;FFFFFF)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T drownedEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(drownedEntity);
        if(token == null){
            token = new OverrideToken(accessed.getTypeId(), this.getOriginal(), OverrideToken.TokenTypes.feature);
            accessed.storeToken(token);
        }
        if(accessed.retrieveOverride(token).isPresent() && !drownedEntity.isInvisible()){
            Identifier newSkin = accessed.retrieveOverride(token).get();
            render(getContextModel(), model, newSkin, matrixStack, vertexConsumerProvider, i, drownedEntity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }

    @Override
    public Identifier getOriginal() {
        return SKIN;
    }
}
