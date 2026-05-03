package com.harrisonandtimmy.alphabetachessengine.model;

public class Square {
  private final int row;
  private final int col;

  public Square(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return this.row;
  }

  public int getCol() {
    return this.col;
  }

  public boolean isOnBoard() {
    return this.row >= 0 && this.row < 8 && this.col >= 0 && this.col < 8;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Square)) {
      return false;
    }

    Square other = (Square) obj;
    return this.row == other.row && this.col == other.col;
  }

  @Override
  public int hashCode() {
    return (8 * this.row) + this.col;
  }

  @Override
  public String toString() {
    int row = 8 - this.row;
    char col = (char) ('a' + this.col);
    return "" + col + row;
  }
}
