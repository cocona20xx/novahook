package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseMarkingFeatureRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(HorseMarkingFeatureRenderer.class)
public abstract class HorseMarkingsMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> implements FeatureRendererAccessor {

    @Shadow @Final private static Map<HorseMarking, Identifier> TEXTURES;
    @Unique private HorseMarking lastMarking = null;
    @Unique private HorseMarking currentMarking = HorseMarking.NONE;
    @Unique private OverrideToken token = null;

    public HorseMarkingsMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
    }

    @Override
    public @Nullable Identifier getOriginal() {
        return TEXTURES.get(currentMarking);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HorseEntity horseEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if(!horseEntity.isInvisible()) {
            currentMarking = horseEntity.getMarking();
            EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(horseEntity);
            if (!Objects.equals(lastMarking, currentMarking)) {
                if (token != null) accessed.clearToken(token);
                token = null;
            }
            lastMarking = currentMarking;
            if (!currentMarking.equals(HorseMarking.NONE)) {
                if (token == null) {
                    token = new OverrideToken(accessed.getTypeId(), getOriginal(), OverrideToken.TokenTypes.feature);
                    accessed.storeToken(token);
                }
                if (accessed.retrieveOverride(token).isPresent()) {
                    Identifier newSkin = accessed.retrieveOverride(token).get();
                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(newSkin));
                    this.getContextModel().render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(horseEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
                    ci.cancel();
                }
            }
        }
    }
}
