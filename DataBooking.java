public class DataBooking {
    String nama, tanggal, jam, durasi, lapangan;

    public DataBooking(String nama, String tanggal, String jam, String durasi, String lapangan) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.jam = jam;
        this.durasi = durasi;
        this.lapangan = lapangan;
    }

    public int hitungTotalBayar() {
        int hargaPerJam = 75000;
        return Integer.parseInt(durasi) * hargaPerJam;
    }
}
