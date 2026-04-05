package lol.hyper.hyperlib.bstats;

import lol.hyper.hyperlib.HyperLib;
import org.bstats.bukkit.Metrics;

public class HyperStats {

    private final HyperLib hyperLib;
    private final int id;
    private Metrics metrics;

    public HyperStats(HyperLib hyperLib, int id) {
        this.hyperLib = hyperLib;
        this.id = id;
    }

    public void setup() {
        metrics = new Metrics(hyperLib.getPlugin(), id);
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
