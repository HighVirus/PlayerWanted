package it.ivirus.playerwanted.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerWanted {
    private final UUID playerUUID;
    private final String playerName;
    private final UUID playerSubmitter;
    private final String reason;
    private final double reward;
    private final Date date;

    @Override
    public String toString() {
        return "PlayerWanted{" +
                "playerUUID=" + playerUUID +
                ", playerName='" + playerName + '\'' +
                ", playerSubmitter=" + playerSubmitter +
                ", reason='" + reason + '\'' +
                ", reward=" + reward +
                ", date=" + date +
                '}';
    }
}
