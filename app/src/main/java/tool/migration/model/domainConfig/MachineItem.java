package tool.migration.model.domainConfig;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineItem {
   private String NMType;
   private String type;
   private String name;
   private String listenAddress;
   private String listenPort;
   


   public String getNMType() {
    return NMType;
   }
   public void setNMType(String nMType) {
    NMType = nMType;
   }
   public String getType() {
    return type;
   }
   public void setType(String type) {
    this.type = type;
   }
   public String getName() {
    return name;
   }
   public void setName(String name) {
    this.name = name;
   }
   public String getListenAddress() {
    return listenAddress;
   }
   public void setListenAddress(String listenAddress) {
    this.listenAddress = listenAddress;
   }
   public String getListenPort() {
    return listenPort;
   }
   public void setListenPort(String listenPort) {
    this.listenPort = listenPort;
   }



   


}
