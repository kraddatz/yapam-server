package app.yapam.healthcheck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BuildInformation {

    private String buildTime;
    private String javaVersion;
    private String tomcatVersion;
    private String springBootVersion;
    private String applicationVersion;
}
