import java.util.*;

public class Boardspace{

  int item; //Available = 0; greenery = 1; ocean = 2; city = 3;
  int id; //No one = -1
  int i; //horizontal
  int j; //30 degrees up from horizontal
  int[] bonus; //[plants,cards,steel,titanium]
  boolean isOcean;

  //Noctis City = (-2,0)
  //Ocean tiles = (-1,0),(0,0),(1,0),(2,-1),(3,-1),(4,-1),(3,1),(1,3),(0,4),(-1,4),(-3,4),(4,-4)

  public Boardspace(int x, int y){
    i = x;
    j = y;
    id = -1;
    item = 0;
    setBonus();
    setOcean();
  }

  private void setBonus(){
    if (j == 0) bonus = new int[] {2,0,0,0};
    if (j == -1){
      if (i == -2){
        bonus = new int[] {2,0,0,0};
      }else{
        bonus = new int[] {1,0,0,0};
      }
    }
    if (j == 1){
      if (i == 0 || i == 3){
        bonus = new int[] {2,0,0,0};
      }else if (i == -4){
        bonus = new int[] {1,0,0,1}; // Pavonis Mons
      }else{
        bonus = new int[] {1,0,0,0};
      }
    }
    if (j == 2){
      if (i == -4){
        bonus = new int[] {0,1,0,0}; //Ascraeus Mons
      }else if (i == 2){
        bonus = new int[] {0,0,1,0};
      }else{
        bonus = new int[] {0,0,0,0};
      }
    }
    if (j == 4){
      if (i == -1){
        bonus = new int[] {0,1,0,0};
      }else if (i == -3 || i == -4){
        bonus = new int[] {0,0,2,0};
      }else{
        bonus = new int[] {0,0,0,0};
      }
    }
    if (j == 3){
      if (i == 1){
        bonus = new int[] {0,2,0,0};
      }else if (i == -3){
        bonus = new int[] {0,0,1,0}; //Tharsis Tholus
      }else{
        bonus = new int[] {0,0,0,0};
      }
    }
    if (j == -2){
      if (i == 3){
        bonus = new int[] {1,0,0,0};
      }else{
        bonus = new int[] {0,0,0,0};
      }
    }
    if (j == -3){
      if (i == 1 || i == 2) bonus = new int[] {0,1,0,0};
      if (i == -1) bonus = new int[] {0,0,2,0};
      if (i == 4) bonus = new int[] {0,0,0,1};
      if (i == 0 || i == 3) bonus = new int[] {0,0,0,0};
    }
    if (j == -4){
      if (i == 4) bonus = new int[] {0,0,0,2};
      if (i == 0) bonus = new int[] {0,0,1,0};
      if (i == 1) bonus = new int[] {0,0,2,0};
      if (i == 2 || i == 3) bonus = new int[] {0,0,0,0};
    }
  }

  private void setOcean(){
    isOcean = false;
    if (j == 0){
      if (i == 0 || i == 1 || i == -1) isOcean = true;
    }
    if (j == -1){
      if (i == 2 || i == 3 || i == 4) isOcean = true;
    }
    if (j == 4){
      if (i == 0 || i == -1 || i == -3) isOcean = true;
    }
    if (i == 3 && j == 1) isOcean = true;
    if (i == 1 && j == 3) isOcean = true;
    if (i == 4 && j == -4) isOcean = true;
  }

  protected boolean placementRestrict(){
    if (i == -2 && j == 0) return true;
    return isOcean;
  }

  public boolean isAdjacent(Boardspace other){
    if (other.isLegal() == false) return false;
    if (this.j == other.j){
      if (this.i == other.i + 1 || this.i == other.i - 1) return true;
    }
    if (this.i == other.i){
      if (this.j == other.j + 1 || this.j == other.j - 1) return true;
    }
    if (this.i == other.i + 1 && this.j == other.j - 1) return true;
    if (this.i == other.i - 1 && this.j == other.j - 1) return true;
    return false;
  }

  public boolean isLegal(){
    return Boardspace.isLegal(this.i,this.j);
  }

  public static boolean isLegal(int x, int y){
    if ((x < -4) || (x > 4) || (y < -4) || (y > 4)) return false;
    if ((x + y > 4) || (x + y < -4)) return false;
    return true;
  }

  public boolean isBoundary(){
    return (i == 4) || (i == -4) || (j == 4) || (j == -4) || (i + j == 4) || (i + j == -4);
  }

  public Vector<Boardspace> adjacentList(){
    Vector<Boardspace> vec = new Vector<Boardspace>(6);
    if (Boardspace.isLegal(i + 1,j)) vec.add(new Boardspace(i + 1,j));
    if (Boardspace.isLegal(i + 1,j - 1)) vec.add(new Boardspace(i + 1,j - 1));
    if (Boardspace.isLegal(i,j + 1)) vec.add(new Boardspace(i,j + 1));
    if (Boardspace.isLegal(i,j - 1)) vec.add(new Boardspace(i,j - 1));
    if (Boardspace.isLegal(i - 1,j)) vec.add(new Boardspace(i - 1,j));
    if (Boardspace.isLegal(i - 1,j + 1)) vec.add(new Boardspace(i - 1,j + 1));
    return vec;
  }

}
