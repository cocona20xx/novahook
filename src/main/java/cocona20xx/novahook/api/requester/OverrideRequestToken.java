package cocona20xx.novahook.api.requester;

@SuppressWarnings("rawtypes")
public interface OverrideRequestToken<T> {
    void setRepresents(Class ofClass);
    void setOfTokenData(T tokenData);
    Class getRepresents();
    T getTokenData();
    Class getThisClass();
}
