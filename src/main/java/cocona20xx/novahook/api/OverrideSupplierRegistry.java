package cocona20xx.novahook.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public final class OverrideSupplierRegistry {
    private static final HashMap<Identifier, OverrideSupplier> SUPPLIERS = new HashMap<>();

    /**
     * Registers an OverrideSupplier object to the registry. Typically, mods using the NovaHook API should only need one OverrideSupplier.
     * @param supplier The {@link OverrideSupplier} child class object to register.
     * @param supplierId An Identifier representing the added supplier. Should typically be of the same namespace as the calling mod, but this is not required.
     * @return Returns true if the supplier was registered (must be both a unique supplier object, and a unique representing identifier)
     */
    public static boolean register(OverrideSupplier supplier, Identifier supplierId){
        if(!SUPPLIERS.containsKey(supplierId) && !SUPPLIERS.containsValue(supplier)){
            SUPPLIERS.put(supplierId, supplier);
            return true;
        } else return false;
    }

    /**
     * @return Returns true if there is at least one supplier.
     */
    public static boolean hasSuppliers(){
        return SUPPLIERS.size() >= 1;
    }

    /**
     * Searches for an override corresponding to an {@link OverrideToken} over all registered suppliers. If multiple Identifiers are found, one will be randomly selected to return.
     * @param token The OverrideToken being used to find an override Identifier.
     * @param targetEntity The entity for which an override is being found.
     * @return Returns an Optional-wrapped {@link Identifier}, or {@code Optional.empty()} if no override is found.
     * @throws IllegalStateException Thrown when no suppliers are present. Check this first with {@link OverrideSupplierRegistry#hasSuppliers()}
     */
    public static Optional<Identifier> searchForOverride(OverrideToken token, Entity targetEntity) throws IllegalStateException{
        if(!hasSuppliers()) throw new IllegalStateException("NovaHook API - OverrideSupplierRegistry: No override suppliers are registered.");
        else {
            ArrayList<Identifier> potentialReturns = new ArrayList<>();
            for(OverrideSupplier s : SUPPLIERS.values()){
                s.getPossibleOverrideFor(token, targetEntity).ifPresent(potentialReturns::add);
            }
            if(potentialReturns.isEmpty()){
                return Optional.empty();
            } else if (potentialReturns.size() == 1){
                return Optional.of(potentialReturns.get(0));
            } else {
                WeightedList<Identifier> randomizer = new WeightedList<>();
                for(Identifier idx : potentialReturns){
                    randomizer.add(idx, 1);
                }
                Identifier idr = randomizer.shuffle().stream().findAny().orElse(null);
                if(idr == null) return Optional.empty();
                else return Optional.of(idr);
            }
        }
    }

    //UNUSED AS OF 1.0.0, MAY BE ADDED IN THE FUTURE
//    /**
//     * Provides nearly the same functionality as {@link OverrideSupplierRegistry#searchForOverride(OverrideToken, Entity)}, <I>but only searches for overrides on the specified supplier identifier.</I>
//     * @param supplierId The supplier id which will be used to find an override, via the supplier it corresponds to.
//     * @param token The OverrideToken being used to find an override Identifier.
//     * @param targetEntity The entity for which an override is being found.
//     * @return Returns an Optional-wrapped {@link Identifier}, or {@code Optional.empty()} if no override is found.
//     * @throws IllegalStateException Thrown when no suppliers are present, or if no supplier exists for the passed supplierId.
//     * @see OverrideSupplierRegistry#searchForOverride(OverrideToken, Entity)
//     */
//    public static Optional<Identifier> searchForOverride(OverrideToken token, Entity targetEntity, Identifier supplierId) throws IllegalStateException{
//        if(!hasSuppliers()) throw new IllegalStateException("NovaHook API - OverrideSupplierRegistry: No override suppliers are registered.");
//        else if(!SUPPLIERS.containsKey(supplierId)){
//            throw new IllegalStateException("NovaHook API - OverrideSupplierRegistry: No supplier registered for Identifier " + supplierId.toString());
//        } else {
//            OverrideSupplier supplier = SUPPLIERS.get(supplierId);
//            return supplier.getPossibleOverrideFor(token, targetEntity);
//        }
//    }
}
