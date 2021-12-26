package it.ivirus.playerwanted.data;

import it.ivirus.playerwanted.PlayerWantedMain;
import it.ivirus.playerwanted.util.PlayerWanted;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WantedData {
    private final PlayerWantedMain plugin = PlayerWantedMain.getInstance();
    @Getter
    private final Map<UUID, PlayerWanted> playerWantedMap = new HashMap<>();  //<PlayerUUID, PlayerWanted>

    @Getter
    private final List<PlayerWanted> playerWantedList = new ArrayList<>();

    @Getter(lazy = true)
    private static final WantedData instance = new WantedData();

    public void loadWantedPlayers() {
        plugin.getSql().getWantedList().whenComplete((playerWanteds, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }).thenAccept(playerWanteds -> {
            for (PlayerWanted pw : playerWanteds) {
                playerWantedMap.put(pw.getPlayerUUID(), pw);
                playerWantedList.add(pw);
            }
        });
    }
}
