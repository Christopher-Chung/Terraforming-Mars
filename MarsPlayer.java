import java.util.Vector;

public class MarsPlayer{

  protected int id;
  int vp;
  protected String corp;
  protected int actionsTaken;
  protected int money;
  protected int steel;
  protected int titanium;
  protected int plant;
  protected int energy;
  protected int heat;
  protected int rating; //TM rating

  protected int moneyProduction;
  protected int steelProduction;
  protected int titaniumProduction;
  protected int plantProduction;
  protected int energyProduction;
  protected int heatProduction;

  protected int scienceTags;
  protected int builderTags;
  protected int jovianTags;
  protected int numEvents;
  protected int spaceTags;
  protected int earthTags;
  protected Vector<Integer> cities;
  protected Vector<Integer> greens;
  Vector<MarsCard> hand;
  protected Vector<MarsCard> playedCards;

  public MarsPlayer(int sequence, String corperation){
    id = sequence;
    corp = corperation;


  }

  public void newTurn(){
    actionsTaken = 0;
  }


  public boolean buildPlant(int position){

  }

  private MarsPlayer moneyChange(int change){
    money += change;
    return this;
  }

  private void ratingIncrement(){
    rating ++;
  }

  private void takeAction(){
    actionsTaken ++;
  }

}
