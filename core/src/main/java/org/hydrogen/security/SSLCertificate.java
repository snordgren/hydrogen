package org.hydrogen.security;

import java.util.Optional;

public class SSLCertificate {
    private final String keystoreFile,
            keystorePassword;

    private final Optional<String> truststoreFile,
            truststorePassword;

    public SSLCertificate(String keystoreFile,
            String keystorePassword) {
        this(keystoreFile, keystorePassword, null, null);
    }

    public SSLCertificate(String keystoreFile,
            String keystorePassword,
            String truststoreFile,
            String truststorePassword) {
        this.keystoreFile = keystoreFile;
        this.keystorePassword = keystorePassword;
        this.truststoreFile = Optional.ofNullable(truststoreFile);
        this.truststorePassword = Optional.ofNullable(truststorePassword);
    }

    public String getKeystoreFile() {
        return keystoreFile;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public Optional<String> getTruststoreFile() {
        return truststoreFile;
    }

    public Optional<String> getTruststorePassword() {
        return truststorePassword;
    }
}
