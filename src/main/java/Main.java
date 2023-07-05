import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.kstruct.gethostname4j.Hostname;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Main {
    @Parameter(names = {"-path", "-paths", "-dir", "-p"}, description = "Path or paths to search in.", variableArity = true)
    private static List<String> path = new ArrayList<>();

    @Parameter(names = {"-name", "-names", "-n"}, description = "Name or names of file to search for.", variableArity = true)
    private static List<String> name = new ArrayList<>();

    @Parameter(names = {"-hash", "-h"}, description = "Hash of file to look for.")
    private static String hash;

    @Parameter(names = {"-size", "-s"}, description = "Size of file to look for.")
    private static Long size;

    @Parameter(names = {"-algorithm", "-a", "-algo"}, description = "Hash algorithm used. (default: MD5)")
    private static String algorithm = "MD5";

    @Parameter(names = {"-target", "-t"}, description = "Target server application pushes detected file information to.", required = true)
    private static String target;

    @Parameter(names = {"-query", "-q"}, description = "Query ID for grouping of hits.", required = true)
    private static Long query;

    private static final Logger logger = LogManager.getLogger();

    private static HashFunction hashFunction;

    public static void main(String[] args) {
        System.out.println();
        // Set variables to CLI values
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        // If theres no values given, set a default for both Windows and Linux.
        if (path.size() == 0) {
            if (OSHelper.isWindows()) {
                path.add("C:\\");
            } else if (OSHelper.isUnix()) {
                path.add("/");
            } else {
                throw new RuntimeException("Unable to determine OS");
            }
        }

        if (hash != null) {
            switch (algorithm.toLowerCase()) {
                case "md5":
                    hashFunction = Hashing.md5();
                    break;
                case "sha1":
                    hashFunction = Hashing.sha1();
                    break;
                case "sha256":
                    hashFunction = Hashing.sha256();
                    break;
                case "sha512":
                    hashFunction = Hashing.sha512();
                    break;
            }
        }

        if (name.size() > 0 && hash == null && size == null) {
            findByName();
        } else if (name.size() == 0 && hash != null && size == null) {
            findByHash();
        } else if (name.size() == 0 && hash == null && size != null) {
            findBySize();
        } else if (name.size() > 0 && hash != null && size == null) {
            findByNameAndHash();
        } else if (name.size() > 0 && hash == null && size != null) {
            findByNameAndSize();
        } else if (name.size() == 0 && hash != null && size != null) {
            findByHashAndSize();
        } else if (name.size() > 0 && hash != null && size != null) {
            findByEverything();
        } else {
            findByPath();
        }
    }

    private static void findByName() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    for (String name : name) {
                        if (path.toFile().getName().matches(name)) {
                            fileFound(path);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findByHash() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    if (com.google.common.io.Files.asByteSource(path.toFile())
                            .hash(hashFunction).toString().equals(hash)) {
                        fileFound(path);
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findBySize() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    if (path.toFile().length() == size) {
                        fileFound(path);
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findByNameAndHash() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    for (String name : name) {
                        if (path.toFile().getName().matches(name) && com.google.common.io.Files.asByteSource(path.toFile())
                                .hash(hashFunction).toString().equals(hash)) {
                            fileFound(path);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findByNameAndSize() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    for (String name : name) {
                        if (path.toFile().getName().matches(name) && path.toFile().length() == size) {
                            fileFound(path);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findByHashAndSize() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    if (path.toFile().length() == size && com.google.common.io.Files.asByteSource(path.toFile())
                            .hash(hashFunction).toString().equals(hash)) {
                        fileFound(path);
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findByEverything() {
        for (String path : path) {
            Path p = Paths.get(path);
            FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    for (String name : name) {
                        if (path.toFile().getName().matches(name) && path.toFile().length() == size &&
                                com.google.common.io.Files.asByteSource(path.toFile())
                                        .hash(hashFunction).toString().equals(hash)) {
                            fileFound(path);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            };

            try {
                Files.walkFileTree(p, fv);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findByPath() {
        for (String path : path) {
            File file = new File(path);
            if (file.isDirectory()) {
                directoryFound(Paths.get(path));
            }
        }
    }

    private static void directoryFound(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            DetectedDirectory directory = new DetectedDirectory();

            directory.setName(path.getParent().toString());

            if (OSHelper.isWindows()) {
                directory.setAccess(String.format("R: %s W: %s E: %s", String.valueOf(path.toFile().canRead()),
                        String.valueOf(path.toFile().canWrite()), String.valueOf(path.toFile().canExecute())));
            } else {
                Set<PosixFilePermission> filePerm = Files.getPosixFilePermissions(path);
                directory.setAccess(PosixFilePermissions.toString(filePerm));
            }
            directory.setIp(InetAddress.getLocalHost().getHostAddress());
            directory.setHostname(Hostname.getHostname());
            directory.setLastAccessTime(new Date(attrs.lastAccessTime().toMillis()));
            directory.setCreationTime(new Date(attrs.creationTime().toMillis()));
            directory.setLastWriteTime(new Date(attrs.lastModifiedTime().toMillis()));
            directory.setQuery(query);

            ObjectMapper mapper = new ObjectMapper();

            HttpResponse<JsonNode> response = Unirest.post(target)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(directory))
                    .asJson();

            processResponse(response);

        } catch (IOException | UnirestException e) {
            logger.warn("An error occurred.", e);
        }
    }

    private static void fileFound(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            BasicFileAttributes dirAttrs = Files.readAttributes(path.getParent(), BasicFileAttributes.class);
            DetectedFile detectedFile = new DetectedFile();

            detectedFile.setName(path.toFile().getName());
            detectedFile.setBaseName(com.google.common.io.Files.getNameWithoutExtension(path.toFile().getName()));
            detectedFile.setPath(path.toString());
            detectedFile.setDirectoryName(path.getParent().toString());

            detectedFile.setHostname(Hostname.getHostname());

            detectedFile.setLastAccessTime(new Date(attrs.lastAccessTime().toMillis()));
            detectedFile.setCreationTime(new Date(attrs.creationTime().toMillis()));
            detectedFile.setLastWriteTime(new Date(attrs.lastModifiedTime().toMillis()));

            if (OSHelper.isWindows()) {
                System.out.println("Windows");
                detectedFile.setAccess(String.format("R: %s W: %s E: %s", String.valueOf(path.toFile().canRead()),
                        String.valueOf(path.toFile().canWrite()), String.valueOf(path.toFile().canExecute())));
            } else {
                Set<PosixFilePermission> filePerm = Files.getPosixFilePermissions(path);
                detectedFile.setAccess(PosixFilePermissions.toString(filePerm));
            }

            DetectedDirectory directory = new DetectedDirectory();

            directory.setName(path.getParent().toString());

            if (OSHelper.isWindows()) {
                directory.setAccess(String.format("R: %s W: %s E: %s", String.valueOf(path.toFile().canRead()),
                        String.valueOf(path.toFile().canWrite()), String.valueOf(path.toFile().canExecute())));
            } else {
                Set<PosixFilePermission> filePerm = Files.getPosixFilePermissions(path);
                directory.setAccess(PosixFilePermissions.toString(filePerm));
            }
            directory.setLastAccessTime(new Date(dirAttrs.lastAccessTime().toMillis()));
            directory.setCreationTime(new Date(dirAttrs.creationTime().toMillis()));
            directory.setLastWriteTime(new Date(dirAttrs.lastModifiedTime().toMillis()));

            detectedFile.setDirectory(directory);

            detectedFile.setQueryId(query);

            System.out.println(detectedFile);

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writeValueAsString(detectedFile));

            HttpResponse<JsonNode> response = Unirest.post(target)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(detectedFile))
                    .asJson();

            processResponse(response);
        } catch (IOException | UnirestException e) {
            logger.warn("An error occurred.", e);
        }
    }

    private static void processResponse(HttpResponse<JsonNode> response) {
        if (response.getStatus() == 404) {
            logger.warn("Page Fot Found");
        } else if (response.getStatus() == 401) {
            logger.warn("Unauthorized.");
        } else if (response.getStatus() == 500) {
            logger.warn("Internal Server Error");
        }
    }
}