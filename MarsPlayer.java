import java.util.Vector;

public class MarsPlayer{

  protected int id;
  int vp;
  String corp;
  boolean hardPass;
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
  Vector<MarsCard> playedEvents;
  Vector<MarsCard> playedPermanents;
  Vector<MarsCard> playedProjects;
  Vector<MarsCard> tempDraftHand;
  Vector<MarsCard> draftHand;

  public MarsPlayer(int sequence, String corperation){
    id = sequence;
    corp = corperation;
  }

  protected void generationProduce(){
    money += (rating + moneyProduction);
    heat += (energy + heatProduction);
    energy = energyProduction;
    plant += plantProduction;
    steel += steelProduction;
    titanium += titaniumProduction;
  }

  protected MarsPlayer moneyChange(int change){
    money += change;
    return this;
  }

  protected void ratingIncrement(){
    rating ++;
  }

}
