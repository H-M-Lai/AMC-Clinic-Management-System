package javaproj.Methods.IdGenerator;

import java.util.List;

public interface IdGenerator{
    String nextId(List<String> idList, String prefix);
}
