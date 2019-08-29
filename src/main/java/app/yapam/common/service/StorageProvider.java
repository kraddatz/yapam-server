package app.yapam.common.service;

import app.yapam.file.model.File;

public interface StorageProvider {

    File readFile(String fileId);

    void storeFile(byte[] data, String fileId);
}
