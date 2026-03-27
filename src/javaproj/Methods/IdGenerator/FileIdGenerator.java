package javaproj.Methods.IdGenerator;

import java.util.List;

public class FileIdGenerator implements IdGenerator {
    @Override
    public String nextId(List<String> idList, String prefix){
        int max = 0;
        if (idList != null) {
            for (String id : idList){
                if (id == null) continue;
                if(!id.startsWith(prefix)) continue;
                try{
                    int num = Integer.parseInt(id.substring(prefix.length()));
                    if (num > max) max = num;
                }catch (NumberFormatException e){
                    // ignore invalid IDs

                }
            }
        }
        return String.format("%s%04d",prefix, max + 1); 
    }
}
