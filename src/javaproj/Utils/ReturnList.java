package javaproj.Utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReturnList {
    private ReturnList() {}
     
    // Maps a logical list name to its backing text file.
    private static Path resolve(String name){
         if (name == null) return Paths.get("data/unknown.txt");
         String k = name.trim().toLowerCase();
         switch (k) {
            case "specialty":
                return Paths.get("data/specialty.txt");
            case "servicetype":
                return Paths.get("data/serviceType.txt");
            case "therapeuticclass":
                return Paths.get("data/therapeuticClass.txt");
            default:
                return Paths.get(name);
        }
     }
     
    public static List<String> get(String name) {
        Path p = resolve(name);
        ensureFile(p);
        
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(p)){
            String line;
            while ((line = reader.readLine()) != null){
                if (!line.trim().isEmpty()) {
                    list.add(line.trim());
                }
            }
            
        } catch (IOException e){
            System.out.println("Fail to load txt file");
        }
        return list;
    }
    
    public static boolean add(String name, String value) {
        if (value == null || value.trim().isEmpty()) return false;
        Path p = resolve(name);
        List<String> list = get(name);
        String v = value.trim();
        if (list.contains(v)) return false;
        list.add(v);
        return save(p, list);
    }
    public static boolean remove(String name, String value) {
        if (value == null) return false;
        Path p = resolve(name);
        List<String> list = get(name);
        boolean changed = list.remove(value);
        return changed && save(p, list);
    }
    private static boolean save(Path p, List<String> list) {
        ensureFile(p);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (String s : list) {
                bw.write(s);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Fail to save txt file: " + p);
            return false;
        }
    }
    
    
    private static void ensureFile(Path p) {
        try {
            Path parent = p.getParent();
            if (parent != null) Files.createDirectories(parent);
            if (Files.notExists(p)) Files.createFile(p);
        } catch (IOException ignored) {}
    }
}
