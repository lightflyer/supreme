package ambm.setting;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SettingTable {

    public SettingTable() {
    }

    @Getter @Setter public int id;
    @Getter @Setter public String ip;
    @Getter @Setter public String port;
    @Getter @Setter public String proxyUsername;
    @Getter @Setter public String proxyPassword;
    @Getter @Setter public String username;
    @Getter @Setter public String password;
    @Getter @Setter public boolean enabled;

    public SettingTable(String ip, String port, String proxyUsername, String proxyPassword, String username, String password, boolean enabled) {
        this.ip = ip;
        this.port = port;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}


