package sample.metrics.influx;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.export.influx.InfluxProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Set;

/**
 * Configuration for exporting metrics to Influx.
 *
 * @author Mariusz Sondecki
 */
@Configuration
@ConditionalOnProperty(prefix = "management.metrics.export.influx", name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(InfluxProperties.class)
public class InfluxMetricsExportConfiguration {

    /**
     * Prefixes based on which metrics will be grouped and shipped to Influx
     * in line protocol {@see https://docs.influxdata.com/influxdb/v1.7/concepts/glossary/#line-protocol}
     * where prefix will be target metric name and names of grouped metrics after extracting prefix will be target field names.
     */
    @Value("${management.metrics.export.influx.prefixes:#{T(java.util.Collections).empty()}}")
    private Set<String> prefixes;

    @Primary
    @Bean
    public InfluxMeterRegistry influxMeterRegistry(InfluxConfig influxConfig, Clock clock,
                                                   InfluxProperties properties) {
        return InfluxMeterRegistry.builder(influxConfig).clock(clock).httpClient(
                new HttpUrlConnectionSender(properties.getConnectTimeout(), properties.getReadTimeout()))
                .prefixes(prefixes)
                .build();

    }
}
