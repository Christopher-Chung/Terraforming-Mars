import java.util.*;

public class MarsBoard{

  HashMap<Integer,Boardspace> getCoordinates; //100 * x + y
  boolean phobosHaven;
  boolean ganymede;

  public MarsBoard(){
    phobosHaven = false;
    ganymede = false;

    getCoordinates = new HashMap<Integer,Boardspace>();
    for (int i = -4; i <= 4; i ++){
      for (int j = -4; j <= 4; j ++){
        if (Boardspace.isLegal(i,j)){
          getCoordinates.put(100 * i + j,new Boardspace(i,j));
        }
      }
    }
  }
  //Not finished!!!
  private boolean checkLocation(int x, int y, int item, int id){
    Boardspace space = getCoordinates.get(100*x + y);
    if (space.isLegal() == false || space.item != 0) return false;
    if (item == 2) return space.isOcean;
    if (item == 3){
      Vector<Boardspace> adjacent = space.adjacentList();
      for (Boardspace neighbor : adjacent){
        if (neighbor.item == 3) return false;
      }
      return true;
    }
    if (item == 1){
      Vector<Boardspace> adjacent = space.adjacentList();
      //Greeneries must be next to own tiles
      for (Boardspace neighbor : adjacent){
        if (neighbor.id == id) return true;
      }
      return false; //Change
    }
    return true;
  }

}
