package com.company.security;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.security.Credentials;

import java.io.IOException;

public class UsernamePasswordCredentials implements Credentials, Portable {
    public static final int CLASS_ID = 1;

    private String username;
    private String password;
    private String endpoint;

    public UsernamePasswordCredentials() {
    }

    public UsernamePasswordCredentials(String username, String password, String endpoint) {
        this.username = username;
        this.password = password;
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getPrincipal() {
        return getUsername();
    }

    public String getUsername(){
        return username;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public void readPortable(PortableReader portableReader) throws IOException {
        username = portableReader.readUTF("username");
        password = portableReader.readUTF("password");
        endpoint = portableReader.readUTF("endpoint");
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
        portableWriter.writeUTF("username", username);
        portableWriter.writeUTF("password", password);
        portableWriter.writeUTF("endpoint", endpoint);
    }

    @Override
    public int getClassId() {
        return UsernamePasswordCredentials.CLASS_ID;
    }

    @Override
    public int getFactoryId() {
        return UsernamePasswordCredentialsFactory.FACTORY_ID;
    }
}
