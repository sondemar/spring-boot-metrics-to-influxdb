package org.springframework.boot.actuate.autoconfigure.metrics.export.influx;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnProperty(prefix = "management.metrics.export.influx", name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(InfluxProperties.class)
public class InfluxMetricsExportCustomAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InfluxConfig influxConfig(InfluxProperties properties) {
        return new InfluxPropertiesConfigAdapter(properties);
    }

    @Bean
    public InfluxMeterRegistry influxMeterRegistry(InfluxConfig influxConfig, Clock clock,
                                                   InfluxProperties properties) {
        return InfluxMeterRegistry.builder(influxConfig).clock(clock).httpClient(
                new HttpUrlConnectionSender(properties.getConnectTimeout(), properties.getReadTimeout()))
                .prefixes(Collections.singleton("heap"))
                .build();

    }
}
