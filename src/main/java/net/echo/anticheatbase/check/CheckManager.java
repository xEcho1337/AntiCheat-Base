package net.echo.anticheatbase.check;

import com.google.common.collect.ImmutableClassToInstanceMap;
import net.echo.anticheatbase.check.impl.misc.TransactionOrder;
import net.echo.anticheatbase.check.impl.module.LatencyModule;
import net.echo.anticheatbase.check.impl.module.PositionModule;
import net.echo.anticheatbase.check.model.AbstractCheck;
import net.echo.anticheatbase.player.model.WrappedPlayer;

import java.util.Collection;

public class CheckManager {

    private final ImmutableClassToInstanceMap<AbstractCheck> checkList;

    public CheckManager(WrappedPlayer player) {
        this.checkList = ImmutableClassToInstanceMap
                .<AbstractCheck>builder()
                .put(TransactionOrder.class, new TransactionOrder(player))
                .put(LatencyModule.class, player.getLatencyModule())
                .put(PositionModule.class, player.getPositionModule())
                .build();
    }

    public <T extends AbstractCheck> T getCheck(Class<T> clazz) {
        return checkList.getInstance(clazz);
    }

    public Collection<AbstractCheck> getAllChecks() {
        return checkList.values();
    }
}
