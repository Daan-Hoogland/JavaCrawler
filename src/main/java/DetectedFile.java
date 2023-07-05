import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class DetectedFile {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("BaseName")
    private String baseName;
    @JsonProperty("FullName")
    private String path;
    @JsonProperty("DirectoryName")
    private String directoryName;

    @JsonProperty("Hostname")
    private String hostname;
    @JsonProperty("IP")
    private String ip = InetAddress.getLocalHost().getHostAddress();

    @JsonProperty("CreationTime")
    private Date creationTime;
    @JsonProperty("LastAccessTime")
    private Date lastAccessTime;
    @JsonProperty("LastWriteTime")
    private Date lastWriteTime;
    @JsonProperty("Access")
    private String access;

    @JsonProperty("Directory")
    private DetectedDirectory directory;

    @JsonProperty("QueryID")
    private Long queryId;

    public DetectedFile() throws UnknownHostException {
    }

    public DetectedFile(String name, String baseName, String path, String directoryName, String hostname, String ip, Date creationTime, Date lastAccessTime, Date lastWriteTime, String access, DetectedDirectory directory, Long queryId) throws UnknownHostException {
        this.name = name;
        this.baseName = baseName;
        this.path = path;
        this.directoryName = directoryName;
        this.hostname = hostname;
        this.ip = ip;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.lastWriteTime = lastWriteTime;
        this.access = access;
        this.directory = directory;
        this.queryId = queryId;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("BaseName")
    public String getBaseName() {
        return baseName;
    }

    @JsonProperty("BaseName")
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    @JsonProperty("FullName")
    public String getPath() {
        return path;
    }

    @JsonProperty("FullName")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("DirectoryName")
    public String getDirectoryName() {
        return directoryName;
    }

    @JsonProperty("DirectoryName")
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    @JsonProperty("Hostname")
    public String getHostname() {
        return hostname;
    }

    @JsonProperty("Hostname")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @JsonProperty("IP")
    public String getIp() {
        return ip;
    }

    @JsonProperty("CreationTime")
    public Date getCreationTime() {
        return creationTime;
    }

    @JsonProperty("CreationTime")
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("LastAccessTime")
    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    @JsonProperty("LastAccessTime")
    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @JsonProperty("LastWriteTime")
    public Date getLastWriteTime() {
        return lastWriteTime;
    }

    @JsonProperty("LastWriteTime")
    public void setLastWriteTime(Date lastWriteTime) {
        this.lastWriteTime = lastWriteTime;
    }

    @JsonProperty("Access")
    public String getAccess() {
        return access;
    }

    @JsonProperty("Access")
    public void setAccess(String access) {
        this.access = access;
    }

    @JsonProperty("Directory")
    public DetectedDirectory getDirectory() {
        return directory;
    }

    @JsonProperty("Directory")
    public void setDirectory(DetectedDirectory directory) {
        this.directory = directory;
    }

    @JsonProperty("QueryID")
    public Long getQueryId() {
        return queryId;
    }

    @JsonProperty("QueryID")
    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    @Override
    public String toString() {
        return "DetectedFile{" +
                "name='" + name + '\'' +
                ", baseName='" + baseName + '\'' +
                ", path='" + path + '\'' +
                ", directoryName='" + directoryName + '\'' +
                ", hostname='" + hostname + '\'' +
                ", ip='" + ip + '\'' +
                ", creationTime=" + creationTime +
                ", lastAccessTime=" + lastAccessTime +
                ", lastWriteTime=" + lastWriteTime +
                ", access='" + access + '\'' +
                ", directory=" + directory +
                ", queryId=" + queryId +
                '}';
    }
}
