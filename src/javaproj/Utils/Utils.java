package javaproj.Utils;

import java.awt.Component;
import java.awt.image.BufferedImage;
import javaproj.Methods.Parser.LineParser;
import java.nio.file.*;
import java.io.*;
import java.util.*;

import java.util.function.Function;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Utils {
    // Reads a data file and parses each row after the header.
    public static <T> List<T> readFile(String filename,LineParser<T> parser) {
        List<T> list = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))){            
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine){
                    firstLine = false;
                    continue;
                }
                list.add(parser.parse(line));
            }
     }catch (IOException e){
         e.printStackTrace();
     }
        return list;
    }   
    
    // Writes the updated rows back to the file and keeps the original header.
    public static <T> void writeFile(String filename, List<T> list){
        String header = "";
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(filename))){
            header = reader.readLine();
        }catch (IOException e){
             e.printStackTrace();
        }
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))){
            writer.write(header + "\n");
            for(T item : list){
                writer.write(item.toString());
                writer.newLine();
            }
        }catch (IOException e){
            System.err.println("Failed to write to file "+ filename);
            e.printStackTrace();
        }
    }
    
    // Rebuilds a table model from the current list.
    public static <T> void refreshTable(JTable table, List<T> data, Function<T, Object[]> rowMapper){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (T item : data) {
            model.addRow(rowMapper.apply(item));
        }
    }
    
    // Finds the next ID for records that share the same prefix.
    public static <T> String idIncrement(List<T> list, String prefix){
        int max = 0;
        for (T item : list){
            String line = item.toString();
            String id = line.split(",")[0];
            if(id.startsWith(prefix)){
                try{
                    int num = Integer.parseInt(id.substring(prefix.length()));
                    if (num > max) max = num;
                }catch (NumberFormatException e){
                    // Skip malformed IDs and keep scanning.
                }
            }
        }
        
        return String.format("%s%04d",prefix, max + 1);
    }

    // Replaces the items shown in a combo box.
    public static void updateComboBox(JComboBox<String> comboBox, List<String> items){
        comboBox.setModel(new DefaultComboBoxModel<>(items.toArray(new String[0])));
    }
    
    // Looks for a profile image whose filename starts with the user ID.
    private static String loadProfilePic(String id){
        Path folder = Paths.get("profilePic");
        Path defaultImage = folder.resolve("default.jpg");

        if (!Files.isDirectory(folder)) {
            return defaultImage.toString();
        }

        try (java.util.stream.Stream<Path> files = Files.list(folder)) {
            return files.filter(Files::isRegularFile)
                        .filter(p -> {
                            String n = p.getFileName().toString().toLowerCase();
                            return n.endsWith(".jpg") || n.endsWith(".jpeg")
                                || n.endsWith(".png") || n.endsWith(".gif")
                                || n.endsWith(".bmp") || n.endsWith(".webp");
                        })
                        .filter(p -> p.getFileName().toString()
                                      .toLowerCase()
                                      .startsWith(id.toLowerCase()))
                        .map(Path::toString)
                        .findFirst()
                        .orElse(defaultImage.toString());
        } catch (java.io.IOException e) {
            return defaultImage.toString();
        }
    }
    
    // Loads the selected profile image.
    private static BufferedImage readProfileImage(String userId) {
        try {
            return ImageIO.read(new File(loadProfilePic(userId)));
        } catch (Exception e) {
            try {
                return ImageIO.read(new File("profilePic/default.jpg"));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    // Updates a label icon on the EDT.
    public static void setProfileIcon(JLabel label, String userId) {
        BufferedImage img = readProfileImage(userId);
        SwingUtilities.invokeLater(() -> {
            label.setIcon(img != null ? new ImageIcon(img) : null);
            label.revalidate();
            label.repaint();
        });
    }
    
    // Expands table columns to fit the current content.
    public static void resizeColumnWidth(JTable table) {
        if (table.getColumnCount() == 0) return;

        final TableColumnModel columnModel = table.getColumnModel();

        for (int col = 0; col < table.getColumnCount(); col++) {
            int width = 50;

            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, table.getColumnName(col), false, false, 0, col);
            width = Math.max(width, headerComp.getPreferredSize().width + 10);

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
                Component c = table.prepareRenderer(cellRenderer, row, col);
                width = Math.max(width, c.getPreferredSize().width + 10);
            }

            columnModel.getColumn(col).setPreferredWidth(Math.min(width, 400));
        }
    }
}

