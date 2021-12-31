package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.OverrideRequestLimiter;
import cocona20xx.novahook.api.OverrideSupplierRegistry;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;


@SuppressWarnings("rawtypes")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements EntityOverrideDataAccessor {

    @Unique private Identifier typeId = null;
    @SuppressWarnings("UnstableApiUsage")
    @Unique private final ListMultimap<OverrideToken, Identifier> overrideMap = MultimapBuilder.linkedHashKeys().arrayListValues().build();
    @Unique private boolean isClientSide = false;
    @Unique private final ArrayList<OverrideToken> possibleOverrideTokens = new ArrayList<>();
    @Unique private OverrideRequestLimiter limiter = null;


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>",
            at = @At("RETURN")
    )
    public void onConstructor(EntityType type, World world, CallbackInfo ci){
        if(world.isClient){
            if(this.typeId == null) typeId = type.getLootTableId();
            isClientSide = true;
            limiter = new OverrideRequestLimiter(MinecraftClient.getInstance());
        }
    }

    @Override
    public Identifier getTypeId() {
        return typeId;
    }

    @Override
    public void storeToken(OverrideToken token) {
        if(isClientSide) {
            if (!this.possibleOverrideTokens.contains(token)) {
                boolean flagAsPossibleMemLeak = false; //acts as failsafe to prevent memory leak from unused tokens; should never happen hopefully?
                for(OverrideToken t : possibleOverrideTokens){
                    if (t.equals(token)) {
                        flagAsPossibleMemLeak = true;
                        break;
                    }
                }
                if(!flagAsPossibleMemLeak) possibleOverrideTokens.add(token);
            }
        }
    }

    @Override
    public void clearToken(OverrideToken token) {
        if(isClientSide) {
            if(this.possibleOverrideTokens.contains(token)) {
                this.possibleOverrideTokens.remove(token);
            }
            if(this.overrideMap.containsKey(token)){
                this.overrideMap.removeAll(token);
            }
        }
    }

    @Override
    public Optional<Identifier> retrieveOverride(OverrideToken token) {
        Identifier returnValue = null;
        if(this.isClientSide){
            if(!possibleOverrideTokens.contains(token)) this.storeToken(token);
            if(limiter.doTimeCheck()){
                //just check all override tokens at once, since doTimeCheck will run every client-tick
                for(OverrideToken t : possibleOverrideTokens){
                    Identifier possibleOverride = OverrideSupplierRegistry.searchForOverride(t, this).orElse(null);
                    if(possibleOverride != null){
                        if(this.overrideMap.containsKey(t)) {
                            Identifier toReplace = this.overrideMap.get(t).get(0);
                            this.overrideMap.remove(t, toReplace);
                        }
                        this.overrideMap.put(t, possibleOverride);
                    } else {
                        if(this.overrideMap.containsKey(t)){
                            this.clearToken(t);
                        }
                    }
                }
            }
            if(this.overrideMap.containsKey(token)){
                returnValue = overrideMap.get(token).get(0);
            } else {
               if(this.overrideMap.keySet().stream().anyMatch(token::equals)){ //sanity check to (again) hopefully stop mem leaks
                   OverrideToken alternate = null;
                   for(OverrideToken o : overrideMap.keySet()){
                       if(o.equals(token)) alternate = o;
                       break;
                   }
                   if(alternate != null){ //sanity check, should never be false
                       returnValue = overrideMap.get(alternate).get(0);
                       overrideMap.removeAll(alternate);
                   }
               }
            }
        }
        return Optional.ofNullable(returnValue);
    }

    @Override
    public void clearAll() {
        if(this.isClientSide) {
            this.overrideMap.clear();
            this.possibleOverrideTokens.clear();
        }
    }
}
