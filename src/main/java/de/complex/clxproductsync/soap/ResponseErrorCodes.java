package de.complex.clxproductsync.soap;

public enum ResponseErrorCodes {
    AUTH_REQUIRED,
    UNKNOWN_ERROR,
    INVALID_KEY,
    UNKNOWN_FILE_UPLOAD_ERROR,
    INVALID_FILENAME,
    UNKNOWN_FREMDKZ,
    DATASET_NOT_FOUND_DELETEJOB_OPEN,
    DATASET_NOT_FOUND;

    public static boolean isDatasetNotFoundDeleteJobOpen(String faultCode){
        return DATASET_NOT_FOUND_DELETEJOB_OPEN.name().equalsIgnoreCase(faultCode);
    }

}
