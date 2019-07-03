package android.epita.fr.ballgame.models;

import android.view.Gravity;

public class GridModel {
    int gridValue;

    public GridModel(int gridValue)
    {
        this.gridValue=gridValue;
    }

    public int getGridValue() {
        return gridValue;
    }

    public void setGridValue(int gridValue) {
        this.gridValue = gridValue;
    }
}
