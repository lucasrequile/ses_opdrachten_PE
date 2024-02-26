package be.kuleuven;

import java.util.*;

public class CheckNeighboursInGrid {
    public CheckNeighboursInGrid() {

    }

    /**
     * This method takes a 1D Iterable and an element in the array and gives back an iterable containing the indexes of all neighbours with the same value as the specified element
     *
     * @param grid         - This is a 1D Iterable containing all elements of the grid. The elements are integers.
     * @param width        - Specifies the width of the grid.
     * @param height       - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     * @param indexToCheck - Specifies the index of the element which neighbours that need to be checked
     * @return - Returns a 1D Iterable of ints, the Integers represent the indexes of all neighbours with the same value as the specified element on index 'indexToCheck'.
     */
    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid, int width, int height, int indexToCheck) {
        List<Integer> gridList = new ArrayList<>();
        for (int value : grid) {
            gridList.add(value);
        }
        //calculate the x and y coordinates of the element to check
        int x = indexToCheck % width;
        int y = indexToCheck / width;

        //calculate the indices of the neighbours
        int[] offsets = {-width-1,-width, -width + 1, -1, 1, width-1, width,  width + 1};
        ArrayList<Integer> indicesToCheck = new ArrayList<Integer>();
        for (int offset : offsets) {
            int newIndex = indexToCheck + offset;
            int newX = newIndex % width;
            int newY = newIndex / width;
            // Check if the new index is valid
            if (newIndex >= 0 && newIndex < width * height && Math.abs(newX - x) <= 1 && Math.abs(newY - y) <= 1) {
                indicesToCheck.add(newIndex);
            }
        }

        //find value of indexed element
        int checkValue = gridList.get(indexToCheck);

        //find values of indices to check and compare to checkValue
        ArrayList<Integer> indicesSameValue = new ArrayList<Integer>();
        for (int currentIndex : indicesToCheck) {
            if (currentIndex >= 0 && currentIndex < width * height) {
                int value = gridList.get(currentIndex);
                if (value == checkValue) {
                    indicesSameValue.add(currentIndex);
                }
            }
        }
        Iterable<Integer> returningIndices = indicesSameValue;
        return returningIndices;
    }
}