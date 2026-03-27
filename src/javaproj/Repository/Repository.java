package javaproj.Repository;

import java.io.IOException;
import java.util.*;

public interface Repository<T> {
    List<T> findAll() throws IOException;
    Optional<T> findById(String id) throws IOException;
    void saveAll(List<T> items) throws IOException;
}