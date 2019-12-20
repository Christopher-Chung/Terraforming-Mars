

public class MarsCard{

  protected int vp;
  protected int cost;
  protected String type; //"Events", "Permanent", "Project"
  protected String name;

  //For global requirements
  int cardReqs; //0 for no reqs, 1 for oxygen, 2 for temp, 3 for ocean, 4 for tag reqs, 5 for others.
  int oxygenReqs; //-4 means max 4%; 4 means 4% or more; 0 means no reqs.
  int tempReqs; //Thereshold for temperature requirements; -1 for no requirements.
  boolean tempReqUpperBound; //true means max bound; no bounds then default false.
  int oceanReqs; //-4 means max 4; 4 means min 4; 0 means no reqs.

  boolean multiTime;

  //Tags!

  public MarsCard(String title, int ){

  }



}
