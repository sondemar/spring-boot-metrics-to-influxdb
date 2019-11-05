package sample.metrics.influx;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ConfigurationProperties} for configuring Influx metrics prefixes.
 */
@ConfigurationProperties(prefix = "management.metrics.export.influx")
public class InfluxPrefixesProperties {

    /**
     * Prefixes based on which metrics will be grouped and shipped to Influx
     * in line protocol {@see https://docs.influxdata.com/influxdb/v1.7/concepts/glossary/#line-protocol}
     * where prefix will be target metric name and names of grouped metrics after extracting prefix will be target field names.
     */
    private Map<String, String> prefixes = new HashMap<>();

    public Map<String, String> getPrefixes() {
        return prefixes;
    }
}
