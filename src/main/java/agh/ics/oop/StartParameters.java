package agh.ics.oop;

public class StartParameters {
    public int mapHeight; //0   // skoro jest publiczne, to nie powinno być finalne?
    public int mapWidth; //1
    public int jgHeight; //2
    public int jgWidth; //3

    public int energyPerPlant; //4
    public int energyToCopulate; //5

    public int startEnergy; //6
    public int animalsAtStart; //7
    public int maxEnergy; //8
    public int moveEnergy; //9

    public int refreshTime; //10

    public int lMode; //11
    public int rMode; //12

    public StartParameters(int[] pars) {
        mapHeight = pars[0];
        mapWidth = pars[1];
        double ratio = (float) pars[2]/pars[3];
        jgHeight = (int) (pars[0]*ratio);
        jgWidth = (int) (pars[1]*ratio);
        energyPerPlant = pars[4];
        energyToCopulate = pars[5];
        startEnergy = pars[6];
        animalsAtStart = pars[7];
        maxEnergy = pars[8];
        moveEnergy = pars[9];
        refreshTime = pars[10];
        lMode = pars[11];
        rMode = pars[12];
    }

}
