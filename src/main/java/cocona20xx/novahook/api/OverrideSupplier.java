package cocona20xx.novahook.api;

import cocona20xx.novahook.api.OverrideToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Base interface for all Override Suppliers.
 */
public interface OverrideSupplier {
    /**
     * Method for supplying overrides for textures. Must only return one texture per Override request; different requests with the same token <i>may</i> return differing textures, however.
     * @param token The {@link OverrideToken} object representing the requested texture.
     * @param entity The {@link Entity} for which a LivingEntityRenderer mixin is requesting a texture override.<br>Child classes should typically ensure that the token and entity are of the same entity type.
     * @return Returns either an empty {@link Optional} object if no overrides are found, or an Optional object wrapping the selected override texture's {@link Identifier} if an override is found.
     */
    Optional<Identifier> getPossibleOverrideFor(OverrideToken token, Entity entity);
}
