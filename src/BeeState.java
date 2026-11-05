public interface BeeState {
    default void enter(BeeWorker bee) {}

    void update(BeeWorker bee, float deltaTime);

    default void exit(BeeWorker bee) {}
}
//if you want to add a new bee state impliment this interface