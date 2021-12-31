package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CatCollarFeatureRenderer;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CatCollarFeatureRenderer.class)
public abstract class CatCollarMixin extends FeatureRenderer<CatEntity, CatEntityModel<CatEntity>> implements FeatureRendererAccessor {

    @Shadow @Final private static Identifier SKIN;
    @Shadow @Final private CatEntityModel<CatEntity> model;
    @Unique private OverrideToken token = null;

    public CatCollarMixin(FeatureRendererContext<CatEntity, CatEntityModel<CatEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/CatEntity;FFFFFF)V",
            at = @At(value = "TAIL", shift = At.Shift.BEFORE),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CatEntity catEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(catEntity);
        if(token == null) {
            token = new OverrideToken(accessed.getTypeId(), SKIN, OverrideToken.TokenTypes.feature);
            accessed.storeToken(token);
        }
        if(accessed.retrieveOverride(token).isPresent() && !catEntity.isInvisible()){
            float[] fs = catEntity.getCollarColor().getColorComponents();
            render(this.getContextModel(), this.model, accessed.retrieveOverride(token).get(), matrixStack, vertexConsumerProvider, i, catEntity, f, g, j, k, l, h, fs[0], fs[1], fs[2]);
            ci.cancel();
        }
    }

    @Override
    public Identifier getOriginal() {
        return SKIN;
    }
}
