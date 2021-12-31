package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfCollarFeatureRenderer.class)
public abstract class WolfCollarMixin extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> implements FeatureRendererAccessor {
    @Shadow @Final private static Identifier SKIN;
    @Unique private OverrideToken token;

    public WolfCollarMixin(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntity wolfEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if(wolfEntity.isTamed() && !wolfEntity.isInvisible()) {
            EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(wolfEntity);
            if(token == null){
                token = new OverrideToken(accessed.getTypeId(), this.getOriginal(), OverrideToken.TokenTypes.feature);
                accessed.storeToken(token);
            }
            if(accessed.retrieveOverride(token).isPresent()){
                Identifier newId = accessed.retrieveOverride(token).get();
                float[] fs = wolfEntity.getCollarColor().getColorComponents();
                renderModel(this.getContextModel(), newId, matrixStack, vertexConsumerProvider, i, wolfEntity, fs[0], fs[1], fs[2]);
                ci.cancel();
            }
        }
    }

    @Override
    public Identifier getOriginal() {
        return SKIN;
    }
}
