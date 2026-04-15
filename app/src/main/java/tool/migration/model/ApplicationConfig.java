package tool.migration.model;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ApplicationConfig {

    private String name;              // logical app name
    private String sourcePath;        // /u01/apps/app.ear
    private String type;              // EAR / WAR
    private String stagingMode;       // stage / nostage / external_stage
    private List<String> targets;     // clusters or servers
    private Integer deploymentOrder;  // optional

    // getters / setters



    
    
    public boolean isValid() {
        return name != null && sourcePath != null && hasTargets();
    }


    public boolean hasTargets() {
        return targets != null && !targets.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStagingMode() {
        return stagingMode;
    }

    public void setStagingMode(String stagingMode) {
        this.stagingMode = stagingMode;
    }

    public List<String> getTargets() {
        return targets;
    }

    
    public void setTargets(List<String> targets) {
        this.targets = targets;
    }
    

    public Integer getDeploymentOrder() {
        return deploymentOrder;
    }

    public void setDeploymentOrder(Integer deploymentOrder) {
        this.deploymentOrder = deploymentOrder;
    }
}
