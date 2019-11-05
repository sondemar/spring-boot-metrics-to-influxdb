package sample.metrics.influx;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.export.influx.InfluxProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;

/**
 * Configuration for exporting metrics to Influx.
 *
 * @author Mariusz Sondecki
 */
@Configuration
@ConditionalOnProperty(prefix = "management.metrics.export.influx", name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties({InfluxProperties.class, InfluxPrefixesProperties.class})
public class InfluxMetricsExportConfiguration {

    @Primary
    @Bean
    public InfluxMeterRegistry influxMeterRegistry(InfluxConfig influxConfig, Clock clock,
                                                   InfluxProperties properties, InfluxPrefixesProperties prefixesProperties) {
        return InfluxMeterRegistry.builder(influxConfig).clock(clock).httpClient(
                new HttpUrlConnectionSender(properties.getConnectTimeout(), properties.getReadTimeout()))
                .prefixes(new HashSet<>(prefixesProperties.getPrefixes().values()))
                .build();

    }
}
