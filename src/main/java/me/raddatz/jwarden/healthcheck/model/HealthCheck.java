package me.raddatz.jwarden.healthcheck.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@ApiModel
@Getter
@Setter
public class HealthCheck {

    @ApiModelProperty(value = "The name of the component to check", example = "MOD4-LicenseMgmt-Database")
    private String name;
    @ApiModelProperty(value = "True, if the check was successful", example = "false")
    private Boolean successful;
    @ApiModelProperty(value = "The action to test the component", example = "Check if database runs")
    private String action;
    @ApiModelProperty(value = "If not successful, contains the error message", example = "database not reachable")
    private String error;
    @ApiModelProperty(value = "The DateTime of the Check", example = "2017-01-26T12:00:00Z")
    private LocalDateTime date;
}