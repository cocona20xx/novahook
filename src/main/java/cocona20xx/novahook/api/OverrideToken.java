package cocona20xx.novahook.api;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("rawtypes")
public final class OverrideToken {
    @Getter private final Class ofEntity;
    @Getter private final String optionalTarget;
    @Getter private final TokenTypes ofType;
    public enum TokenTypes {base_layer, eye_layer, feature_state}

    public OverrideToken(Class ofEntity, TokenTypes ofType, @Nullable String optionalTarget){
        this.ofEntity = ofEntity;
        this.optionalTarget = optionalTarget;
        this.ofType = ofType;
    }

    public String getTargetKey() throws NullPointerException{
        switch (ofType){
            case base_layer -> {
                return "base";
            }
            case eye_layer -> {
                return "eye";
            }
            case feature_state -> {
                if(optionalTarget == null) throw new NullPointerException("Feature-State OverrideToken objects must have a target key defined.");
                else return optionalTarget;
            }
            default -> throw new AssertionError("This shouldn't be reachable unless a OverrideToken object was created without using the constructor.");
        }
    }

}
