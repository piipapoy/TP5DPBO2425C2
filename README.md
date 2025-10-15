TP5 DPBO

Saya M. Raffa Mizanul Insan dengan NIM 2409119 mengerjakan TP 5 dalam mata kuliah Desain Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak akan melakukan kecurangan seperti yang telah di spesifikasikan Aamiin.

---

## Ringkasan

Tugas ini merupakan pengembangan dari TP4, di mana aplikasi GUI manajemen produk yang sebelumnya menggunakan `ArrayList` sebagai database sementara, kini diintegrasikan dengan database **MySQL**. Semua operasi CRUD (Create, Read, Update, Delete) sekarang berinteraksi langsung dengan database.

## Perbedaan Utama TP5 dari TP4

Secara keseluruhan, desain antarmuka dan struktur kelas `Product` tetap sama. Perubahan utama terletak pada logika *back-end* sebagai berikut:

1.  **Koneksi Database MySQL**: Implementasi `ArrayList` sebagai media penyimpanan data telah **dihapus sepenuhnya**. Program sekarang terhubung langsung ke database MySQL menggunakan JDBC (Java Database Connectivity).

2.  **Kelas `Database.java`**: Dibuat sebuah kelas baru bernama `Database.java` yang bertanggung jawab untuk mengelola koneksi dan eksekusi semua query SQL. Ini membuat kode di `ProductMenu.java` menjadi lebih bersih dan terfokus pada logika antarmuka.

3.  **Operasi CRUD via SQL**: Setiap operasi sekarang menjalankan query SQL:
    * **Read**: `setTable()` menjalankan `SELECT * FROM product` untuk mengisi tabel.
    * **Create**: `insertData()` menjalankan `INSERT INTO product ...`.
    * **Update**: `updateData()` menjalankan `UPDATE product SET ... WHERE id = ...`.
    * **Delete**: `deleteData()` menjalankan `DELETE FROM product WHERE id = ...`.

4.  **Validasi Input**: Ditambahkan logika validasi baru sebelum data dikirim ke database:
    * **Pengecekan Kolom Kosong**: Program akan menampilkan dialog *error* jika ada *field* atau pilihan yang belum diisi saat menekan tombol "Add" atau "Update".
    * **Pengecekan ID Duplikat**: Saat menambah data baru, program akan terlebih dahulu memeriksa ke database apakah ID yang dimasukkan sudah ada. Jika sudah ada, proses akan dibatalkan dan dialog *error* akan muncul.

## Dokumentasi

[Demo Program](https://github.com/piipapoy/TP5DPBO2425C2/blob/main/Dokumentasi/Dokumentasi%20TP5%20DPBO.mp4)
