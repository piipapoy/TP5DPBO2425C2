import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        ProductMenu menu = new ProductMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private int selectedIndex = -1;
    // Sengaja dihapus: ArrayList<Product> listProduct;
    private Database database; // Objek untuk koneksi database

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JRadioButton baruRadioButton;
    private JRadioButton bekasRadioButton;
    private ButtonGroup kondisiButtonGroup;

    public ProductMenu() {
        // Inisialisasi koneksi database
        database = new Database();

        // Inisialisasi ButtonGroup secara manual
        kondisiButtonGroup = new ButtonGroup();
        kondisiButtonGroup.add(baruRadioButton);
        kondisiButtonGroup.add(bekasRadioButton);

        // Sengaja dihapus: populateList();

        // isi tabel produk dari database
        productTable.setModel(setTable());

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        String[] kategoriData = {"???", "Elektronik", "Makanan", "Minuman", "Pakaian", "Alat Tulis", "Aksesoris", "Penyimpanan", "Jaringan", "Perangkat Kantor", "Furniture"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));
        deleteButton.setVisible(false);

        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex != -1) {
                    int response = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        deleteData();
                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // (*) Mouse listener diubah sesuai petunjuk
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = productTable.getSelectedRow();
                String selectedId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String selectedKondisi = productTable.getModel().getValueAt(selectedIndex, 5).toString();

                idField.setText(selectedId);
                namaField.setText(selectedNama);
                hargaField.setText(selectedHarga);
                kategoriComboBox.setSelectedItem(selectedKategori);
                if (selectedKondisi.equals("Baru")) {
                    baruRadioButton.setSelected(true);
                } else {
                    bekasRadioButton.setSelected(true);
                }

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Harga", "Kategori", "Kondisi"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            // Ambil data dari database
            ResultSet resultSet = database.selectQuery("SELECT * FROM product");

            int i = 1;
            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = i;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getDouble("harga");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getString("kondisi");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tmp;
    }

    public void insertData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            String hargaStr = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String kondisi = baruRadioButton.isSelected() ? "Baru" : "Bekas";

            // Validasi: Cek jika ada kolom yang kosong
            if (id.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || kategori.equals("???") || kondisiButtonGroup.getSelection() == null) {
                JOptionPane.showMessageDialog(null, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaStr);

            // Validasi: Cek jika ID sudah ada
            String checkSql = "SELECT * FROM product WHERE id = '" + id + "'";
            ResultSet rs = database.selectQuery(checkSql);
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "ID produk sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query untuk insert data
            String sql = "INSERT INTO product VALUES ('" + id + "', '" + nama + "', " + harga + ", '" + kategori + "', '" + kondisi + "')";
            database.insertUpdateDeleteQuery(sql);

            productTable.setModel(setTable());
            clearForm();
            System.out.println("Insert Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            String hargaStr = hargaField.getText();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String kondisi = baruRadioButton.isSelected() ? "Baru" : "Bekas";

            // Validasi: Cek jika ada kolom yang kosong
            if (id.isEmpty() || nama.isEmpty() || hargaStr.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaStr);

            // Query untuk update data
            String sql = "UPDATE product SET nama = '" + nama + "', harga = " + harga + ", kategori = '" + kategori + "', kondisi = '" + kondisi + "' WHERE id = '" + id + "'";
            database.insertUpdateDeleteQuery(sql);

            productTable.setModel(setTable());
            clearForm();
            System.out.println("Update Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        // Ambil ID dari baris yang dipilih
        String id = productTable.getModel().getValueAt(selectedIndex, 1).toString();

        // Query untuk hapus data
        String sql = "DELETE FROM product WHERE id = '" + id + "'";
        database.insertUpdateDeleteQuery(sql);

        productTable.setModel(setTable());
        clearForm();
        System.out.println("Delete Berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        kondisiButtonGroup.clearSelection();
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    // Sengaja dihapus: Method populateList() tidak diperlukan lagi
}