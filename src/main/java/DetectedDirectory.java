import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Optional;

public class DetectedDirectory {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Access")
    private String access;

    @JsonProperty("CreationTime")
    private Date creationTime;

    @JsonProperty("LastAccessTime")
    private Date lastAccessTime;

    @JsonProperty("LastWriteTime")
    private Date lastWriteTime;

    @JsonProperty("Hostname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hostname;

    @JsonProperty("IP")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ip;

    @JsonProperty("QueryID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long query;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getLastWriteTime() {
        return lastWriteTime;
    }

    public void setLastWriteTime(Date lastWriteTime) {
        this.lastWriteTime = lastWriteTime;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getQuery() {
        return query;
    }

    public void setQuery(Long query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "DirectoryHitJson{" +
                "name='" + name + '\'' +
                ", access='" + access + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", lastAccessTime='" + lastAccessTime + '\'' +
                ", lastWriteTime='" + lastWriteTime + '\'' +
                ", hostname='" + hostname + '\'' +
                ", ip='" + ip + '\'' +
                ", query=" + query +
                '}';
    }

}
