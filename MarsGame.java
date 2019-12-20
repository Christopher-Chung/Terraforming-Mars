import java.util.*;

public class MarsGame{

  protected int numPlayers;
  protected int[][] board; //board[location][id]
  protected int temperature;
  protected int generation;
  protected int oxygen;
  protected int oceans;

  final int maxTemp = 8;
  final int maxOxygen = 14;
  final int maxOcean = 9;
  MarsDeck deck;
  Queue<MarsCard> drawPile;
  Vector<MarsCard> discardPile;
  Vector<MarsPlayer> players;

  HashMap<String,boolean> awards;
  HashMap<String,Integer> milestones;
  int[] awardCost;
  int numAward;
  int numMilestone;

  public MarsGame(int playerNumber){
    numPlayers = playerNumber;
    board = new int[!!][2];
    temperature = -24;
    generation = 1;
    oxygen = 0;
    ocean = 0;
    numAward = 0;
    numMilestone = 0;
    milestones = new HashMap<String,Integer>;
    milestones.put("Terraformer",0);
    milestones.put("Builder",0);
    milestones.put("Mayor",0);
    milestones.put("Gardner",0);
    milestones.put("Planner",0);
    awards = new HashMap<String,boolean>;
    awards.put("Landlord",false);
    awards.put("Banker",false);
    awards.put("Scientist",false);
    awards.put("Miner",false);
    awards.put("Thermalist",false);
    players = new Vector<MarsPlayer>();
    awardCost = new int[]{8,14,20};
    deck = new MarsDeck(true);
    drawPile = new ConcurrentLinkedQueue<MarsCard>();
    discardPile = new Vector<MarsCard>();
  }

  private void shuffle(){

  }

  private void tempIncrease(MarsPlayer plyr){
    if (temperature < maxTemp){
      temperature += 2;
      plyr.ratingIncrement();
    }
  }

  private void oxygenIncrease(MarsPlayer plyr){
    if (oxygen < maxOxygen){
      oxygen ++;
      plyr.ratingIncrement();
    }
  }

  private void putOnBoard(MarsPlayer plyr, String item){
    int location = -1;
    while (location < 0 || board[location][0] != 0){
      System.out.println("Choose location: ");
      location = Scanner.nextInt();
    }
    board[location] = 2;
  }

  //Takes a legal card and check global requirements.
  private boolean checkReqs(String cardName){
    MarsCard card = deck.get(cardName);
    switch (card.cardReqs){
      case 0:
        return true;
      case 1:
        if (card.oxygenReqs > 0){
          return oxygen >= card.oxygenReqs;
        }else{
          return oxygen <= -card.oxygenReqs;
        }
      case 2:
        if (card.tempReqUpperBound){
          return temperature <= card.tempReqs;
        }else{
          return temperature >= card.tempReqs;
        }
      case 3:
        if (card.oceanReqs > 0){
          return oceans >= card.oceanReqs;
        }else{
          return oceans <= -card.oceanReqs;
        }
      case 4,5!!!
    }
  }

  //Not complete!
  public boolean standardProjects(String action, int id){
    MarsPlayer plyr = players.get(id);
    if (plyr.actionsTaken >= 2){
      System.out.println("Action limit met.");
      return false;
    }
    int reduction = 0; //Standard projects card?
    switch (action){
      case "Power Plant":
        if (plyr.money >= 11 - reduction){
          plyr.moneyChange(-11 + reduction);
          plyr.energyProduction ++;
          plyr.takeAction();
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Asteroid":
        if (plyr.money >= 14 - reduction){
          plyr.moneyChange(-14 + reduction);
          tempIncrease(plyr);
          plyr.takeAction();
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Aquafer": //Finish this
        if (plyr.money >= 14 - reduction){
          plyr.moneyChange(-14 + reduction);
          tempIncrease(plyr);
          plyr.takeAction();
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "Greenery":
        if (plyr.money >= 23 - reduction){
          plyr.moneyChange(-23 + reduction);
          putOnBoard(plyr, "Greenery");
          plyr.takeAction();
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      case "City":
        if (plyr.money >= 25 - reduction){
          plyr.moneyChange(-25 + reduction);
          putCity(plyr);
          plyr.moneyProduction ++;
          plyr.takeAction();
          return true;
        }else{
          System.out.println("Not enough money!");
          return false;
        }
      default:
        System.out.println("Action does not exist.");
        return false;
    }
  }

  public boolean playCard(String cardName, int id){
    MarsPlayer plyr = players.get(id);
    if (plyr.actionsTaken >= 2){
      System.out.println("Action limit met.");
      return false;
    }
    if (deck.searchCard(cardName) != null){
      MarsCard card = deck.searchCard(cardName);
      //Check for money reduction
      int reduction = 0;
      int cost = card.cost - reduction;
      plyr.moneyChange(-cost);
      effect!;
      plyr.takeAction();
    }else{
      return false;
    }
  }

  //Status: Complete
  public boolean fundAward(String awardName, int id){
    MarsPlayer plyr = player.get(id);
    if (plyr.actionsTaken >= 2){
      System.out.println("Action limit met.");
      return false;
    }
    if (numAward >= 3){
      System.out.println("No more awards.");
      return false;
    }
    if (awards.containsKey(awardName) == false){
      System.out.println("Wrong award name.");
      return false;
    }
    if (awards.get(awardName)){
      System.out.println("Award already funded.");
      return false;
    }
    if (plyr.money >= awardCost[numAward]){
      plyr.moneyChange(-awardCost[numAward]).takeAction();
      awards.replace(awardName,true);
      numAward ++;
      return true;
    }else{
      System.out.println("Not enough money!");
      return false;
    }
  }

  //Status: Complete
  public boolean claimMilestone(String name, int id){
    MarsPlayer plyr = player.get(id);
    if (plyr.actionsTaken >= 2){
      System.out.println("Action limit met.");
      return false;
    }
    if (numMilestone >= 3){
      System.out.println("No more milestones.");
      return false;
    }
    if (milestones.containsKey(name) == false){
      System.out.println("Wrong milestone name.");
      return false;
    }
    if (milestones.get(name) != 0){
      System.out.println("Milestone already claimed.");
      return false;
    }
    if (plyr.money >= 8){
      switch (name){
        case "Terraformer":
          if (plyr.rating < 35){
            System.out.println("Not qualified.");
            return false;
          }
        case "Builder":
          if (plyr.builderTags < 8){
            System.out.println("Not qualified.");
            return false;
          }
        case "Mayor":
          if (plyr.cities.size() < 3){
            System.out.println("Not qualified.");
            return false;
          }
        case "Gardner":
          if (plyr.greens.size() < 3){
            System.out.println("Not qualified.");
            return false;
          }
        case "Planner":
          if (plyr.hand.size() < 16){
            System.out.println("Not qualified.");
            return false;
          }
      }
      plyr.moneyChange(-8).takeAction();
      milestones.replace(name,id);
      plyr.vp += 5;
      numMilestone ++;
      return true;
    }else{
      System.out.println("Not enough money!");
      return false;
    }
  }

}
