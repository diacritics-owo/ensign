package diacritics.owo.util;

public record ModelParameters(int width, int height, SwallowTailParameters swallowtailParameters) {
  public record SwallowTailParameters(int height, int stepX, int stepY) {
  }
}
