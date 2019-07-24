package com.elepy.uploads;

import com.elepy.exceptions.ElepyConfigException;
import com.elepy.exceptions.ElepyException;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryFileService implements FileService {

    private final String rootFolderLocation;

    private final Tika tika = new Tika();

    public DirectoryFileService(String rootFolderLocation) {
        this.rootFolderLocation = rootFolderLocation;
        ensureDirsMade();
    }


    void ensureDirsMade() {
        Path path = Paths.get(rootFolderLocation);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new ElepyConfigException("Can't create upload folder");
        }
    }

    @Override
    public synchronized void uploadFile(FileUpload file) {
        final Path name = Paths.get(rootFolderLocation + File.separator + file.getName());
        try {
            Files.copy(file.getContent(), name);
        } catch (FileAlreadyExistsException e) {
            throw new ElepyException("FileReference Already Exists: " + file.getName(), 409);
        } catch (IOException e) {
            throw new ElepyException("Failed to upload file: " + file.getName(), 500);
        }
    }

    @Override
    public synchronized Optional<FileUpload> readFile(String id) {
        final Path path = Paths.get(rootFolderLocation + File.separator + id);
        try {

            final FileUpload fileUpload = FileUpload.of(tika.detect(path), Files.newInputStream(path), id, Files.size(path));

            return Optional.of(fileUpload);
        } catch (NoSuchFileException e) {
            return Optional.empty();
        } catch (IOException e) {
            throw new ElepyException("Failed at retrieving file: " + id, 500);
        }
    }

    @Override
    public List<String> listFiles() {
        final Path path = Paths.get(rootFolderLocation);

        try (Stream<Path> files = Files.list(path)) {
            return files.map(Path::getFileName).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ElepyException("Failed to list files", 500);
        }
    }

    @Override
    public void deleteFile(String id) {
        final Path path = Paths.get(rootFolderLocation + File.separator + id);

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new ElepyException("Failed to delete file: " + id, 500);
        }
    }

}
