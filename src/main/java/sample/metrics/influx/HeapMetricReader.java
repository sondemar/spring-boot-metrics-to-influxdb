package sample.metrics.influx;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Component
// http://localhost:8080/actuator/metrics/heap.committed.mb
public class HeapMetricReader {

    private static final String METRIC_NAME = "heap";

    private static final String HEAP_INIT_MB = ".init.mb";
    private static final String HEAP_USED_MB = ".used.mb";
    private static final String HEAP_COMMITTED_MB = ".committed.mb";
    private static final String HEAP_MAX_MB = ".max.mb";

    @Autowired
    public HeapMetricReader(MeterRegistry meterRegistry) {
        final MemoryMXBean memoryUsage = getMemoryUsage();

        Gauge.builder(METRIC_NAME+HEAP_INIT_MB, memoryUsage, this::getInit)
                .register(meterRegistry);

        Gauge.builder(METRIC_NAME+HEAP_USED_MB, memoryUsage, this::getUsed)
                .register(meterRegistry);

        Gauge.builder(METRIC_NAME+HEAP_COMMITTED_MB, memoryUsage, this::getCommitted)
                .register(meterRegistry);

        Gauge.builder(METRIC_NAME+HEAP_MAX_MB, memoryUsage, this::getMax)
                .register(meterRegistry);
    }

    private MemoryMXBean getMemoryUsage() {
        return ManagementFactory.getMemoryMXBean();
    }

    private long toMb(long bytes) {
        return bytes / 1024 / 1024;
    }

    private double getInit(MemoryMXBean memoryUsage) {
        return toMb(memoryUsage.getHeapMemoryUsage().getInit());
    }

    private double getUsed(MemoryMXBean memoryUsage) {
        return toMb(memoryUsage.getHeapMemoryUsage().getUsed());
    }

    private double getCommitted(MemoryMXBean memoryUsage) {
        return toMb(memoryUsage.getHeapMemoryUsage().getCommitted());
    }

    private double getMax(MemoryMXBean memoryUsage) {
        return toMb(memoryUsage.getHeapMemoryUsage().getMax());
    }
}
