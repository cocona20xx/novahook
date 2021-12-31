package cocona20xx.novahook.testing;

import cocona20xx.novahook.api.OverrideSupplier;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * FOR TESTING ONLY, DO NOT EXTEND/USE IN DEPENDENT MODS
 */
public final class TestOverrideSupplier implements OverrideSupplier {
    @Override
    public Optional<Identifier> getPossibleOverrideFor(OverrideToken token, Entity entity) {
        if(token.getOfMobTypeId().equals(EntityOverrideDataAccessor.quickWrap(entity).getTypeId())){
            if (token.getOfMobTypeId().equals(new Identifier("minecraft", "entities/enderman")) && token.getOfTokenType().equals(OverrideToken.TokenTypes.eye_layer)){
                return Optional.of(new Identifier("novahook", "textures/testing/enderman_eyes_test.png"));
            } else if(token.getOfMobTypeId().equals(new Identifier("minecraft", "entities/creeper")) && token.getOfTokenType().equals(OverrideToken.TokenTypes.feature)){
                return Optional.of(new Identifier("novahook", "textures/testing/creeper_armor_test.png"));
            } else if(token.getOfMobTypeId().equals(new Identifier("minecraft", "entities/bee"))){
                if(token.getOfTextureId().equals(new Identifier("minecraft", "textures/entity/bee/bee_angry.png"))){
                    return Optional.of(new Identifier("novahook", "textures/testing/bee_angry_test.png"));
                } else if(token.getOfTokenType().equals(OverrideToken.TokenTypes.eye_layer) && token.getOfTextureId().equals(OverrideToken.MULTI_EYE_POINTER_ID)){
                    return Optional.of(new Identifier("novahook", "textures/testing/bee_emissive_test.png"));
                } else return Optional.empty();
            }
            else if(token.getOfTextureId().equals(new Identifier("textures/entity/sheep/sheep_fur.png")) && token.getOfTokenType().equals(OverrideToken.TokenTypes.feature)){
                if(entity.hasCustomName()){
                    if(entity.getCustomName().asString().equals("jeb_")) {
                        return Optional.of(new Identifier("novahook", "textures/testing/jeb_wool_test.png"));
                    }
                    else return Optional.empty();
                } else return Optional.empty();
            } else return Optional.empty();
        } else return Optional.empty();
    }
}
