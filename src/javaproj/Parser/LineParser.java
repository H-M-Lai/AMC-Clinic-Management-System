package javaproj.Methods.Parser;
public abstract class LineParser<T> {
    // Splits CSV-style rows while keeping quoted commas intact.
    protected String[] splitline(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
    
    public abstract T parse(String line);
}
