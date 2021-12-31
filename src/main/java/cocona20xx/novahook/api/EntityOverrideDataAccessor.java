package cocona20xx.novahook.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface EntityOverrideDataAccessor {

    void storeToken(OverrideToken token);
    Optional<Identifier> retrieveOverride(OverrideToken token);
    void clearToken(OverrideToken token);
    void clearAll();
    static EntityOverrideDataAccessor quickWrap(LivingEntity toWrap){
        return (EntityOverrideDataAccessor)toWrap;
    }
    static EntityOverrideDataAccessor quickWrap(Entity toWrapEntity){
        return (EntityOverrideDataAccessor)toWrapEntity;
    }
    Identifier getTypeId();
}
