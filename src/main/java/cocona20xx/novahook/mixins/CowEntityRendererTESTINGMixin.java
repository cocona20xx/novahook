package cocona20xx.novahook.mixins;

import cocona20xx.novahook.NovaHookClient;
import cocona20xx.novahook.api.StandardOverrideAccessor;
import cocona20xx.novahook.api.StandardOverrideApplier;
import cocona20xx.novahook.api.requester.OverrideRequestToken;
import cocona20xx.novahook.impl.DefaultRequestRateLimiter;
import cocona20xx.novahook.internal.MultiEntityEyeLayerFeature;
import cocona20xx.novahook.internal.testing.TestingRequestToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.CowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowEntityRenderer.class) @SuppressWarnings("rawtypes")
public abstract class CowEntityRendererTESTINGMixin extends LivingEntityRenderer<CowEntity, CowEntityModel<CowEntity>> implements StandardOverrideApplier {

    @Unique
    private final DefaultRequestRateLimiter limiter = new DefaultRequestRateLimiter();
    @Unique
    private MinecraftClient clientInstance = null;
    @Unique
    private final TestingRequestToken testToken = new TestingRequestToken();
    @Unique
    private MultiEntityEyeLayerFeature cowEyeLayerFeature;
    @Unique
    private Identifier eyeOverride = new Identifier("novahook", "textures/empty_sprite.png");
    @Unique
    private Identifier baseOverride = new Identifier("textures/entity/cow/cow.png");


    public CowEntityRendererTESTINGMixin(EntityRendererFactory.Context ctx, CowEntityModel<CowEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }


    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void onConstructor(EntityRendererFactory.Context context, CallbackInfo ci){
        clientInstance = MinecraftClient.getInstance();
        testToken.setOfTokenData(true);
        testToken.setRepresents(CowEntity.class);
        cowEyeLayerFeature = new MultiEntityEyeLayerFeature<>((CowEntityRenderer)(Object)this);
        this.addFeature(cowEyeLayerFeature);
    }

    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/CowEntity;)Lnet/minecraft/util/Identifier;", at = @At(value =  "HEAD"), cancellable = true)
    public void onGetTexture(CowEntity cowEntity, CallbackInfoReturnable<Identifier> cir){
        if(doLimitCheck()){
            checkBaseOverride(cowEntity, this.testToken);
            checkEyeLayerOverride(cowEntity, this.testToken);
            if(((StandardOverrideAccessor) cowEntity).getEyeOverride().isPresent()){
                eyeOverride = ((StandardOverrideAccessor) cowEntity).getEyeOverride().get();
            }
            if(((StandardOverrideAccessor) cowEntity).getBaseOverride().isPresent()){
                baseOverride = ((StandardOverrideAccessor) cowEntity).getBaseOverride().get();
            }
        }
        cowEyeLayerFeature.setCurrentEyesTexture(eyeOverride);
        cir.setReturnValue(baseOverride);
        cir.cancel();
    }

    @Override
    public void checkBaseOverride(LivingEntity selfRef, OverrideRequestToken token) {
        if(!((StandardOverrideAccessor) selfRef).hasRequestInfo()){
            ((StandardOverrideAccessor) selfRef).setRequestInfo(token, selfRef);
            ((StandardOverrideAccessor) selfRef).setRequester(NovaHookClient.requesterTestingOnly);
        }
        ((StandardOverrideAccessor) selfRef).doBaseRequest();
    }

    @Override
    public void checkEyeLayerOverride(LivingEntity selfRef, OverrideRequestToken token) {
        if(!((StandardOverrideAccessor) selfRef).hasRequestInfo()){
            ((StandardOverrideAccessor) selfRef).setRequestInfo(token, selfRef);
            ((StandardOverrideAccessor) selfRef).setRequester(NovaHookClient.requesterTestingOnly);
        }
        ((StandardOverrideAccessor) selfRef).doEyeLayerRequest();
    }

    @Override
    public boolean doLimitCheck() {
        if(clientInstance != null){
            return limiter.doDurationCheck(clientInstance);
        } else return false;
    }
}
