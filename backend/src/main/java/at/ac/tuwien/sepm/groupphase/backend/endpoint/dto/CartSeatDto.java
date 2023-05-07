package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class CartSeatDto {

    int id;
    int row;
    int number;
    int sectorId;

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public int getSectorId() {
        return sectorId;
    }
}
