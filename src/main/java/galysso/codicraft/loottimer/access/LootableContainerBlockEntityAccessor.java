package galysso.codicraft.loottimer.access;

public interface LootableContainerBlockEntityAccessor {
    long getRemainingTicks();
    Boolean isOnClock();
}
