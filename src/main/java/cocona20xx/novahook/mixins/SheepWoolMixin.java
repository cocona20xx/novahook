package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.objectweb.asm.Opcodes.GETSTATIC;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolMixin extends FeatureRenderer<SheepEntity, SheepEntityModel<SheepEntity>> implements FeatureRendererAccessor {
    public SheepWoolMixin(FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>> context) {
        super(context);
    }
    @Shadow @Final private static Identifier SKIN;
    @Unique private OverrideToken token = null;
    @Unique private Identifier localOverrideStorage = null;


    @Override
    public Identifier getOriginal() {
        return SKIN;
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer;SKIN:Lnet/minecraft/util/Identifier;", opcode = GETSTATIC)
    )
    private Identifier mixin(){
        if(localOverrideStorage != null) return localOverrideStorage;
        else return getOriginal();
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V",
        at = @At("RETURN")
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, SheepEntity sheepEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if(!sheepEntity.isSheared()){
            EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(sheepEntity);
            if(token == null){
                token = new OverrideToken(accessed.getTypeId(), getOriginal(), OverrideToken.TokenTypes.feature);
                accessed.storeToken(token);
            }
            if(accessed.retrieveOverride(token).isPresent()){
                this.localOverrideStorage = accessed.retrieveOverride(token).get();
            } else localOverrideStorage = null;
        }
    }


}
